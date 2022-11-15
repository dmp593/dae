package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.requests.PageRequest;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("subjects")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SubjectService {

    @EJB
    private SubjectBean subjectBean;


    @Path("")
    @GET
    public Response all(@BeanParam @Valid PageRequest pageRequest) {
        var count = subjectBean.count();

        if (pageRequest.getOffset() > count) {
            return Response.ok(new PaginatedDTOs<>(count)).build();
        }

        var subjects = subjectBean.all(pageRequest.getOffset(), pageRequest.getLimit());

        var paginatedDTO = new PaginatedDTOs<>(SubjectDTO.from(subjects), count, pageRequest.getOffset());
        return Response.ok(paginatedDTO).build();
    }
}
