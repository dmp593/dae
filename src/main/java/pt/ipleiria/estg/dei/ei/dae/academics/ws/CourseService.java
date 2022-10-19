package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.academics.requests.PageRequest;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("courses")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CourseService {

    @EJB
    private CourseBean courseBean;

    @GET
    @Path("/")
    public Response all(@BeanParam @Valid PageRequest pageRequest) {
        var count = courseBean.count();

        if (pageRequest.getOffset() > count) {
            return Response.ok(new PaginatedDTOs<>(count)).build();
        }

        var courses = courseBean.getAll(pageRequest.getOffset(), pageRequest.getLimit());

        var paginatedDTO = new PaginatedDTOs<>(CourseDTO.from(courses), count, pageRequest.getOffset());
        return Response.ok(paginatedDTO).build();
    }

    @GET
    @Path("{code}")
    public Response get(@PathParam("code") Long code) {
        return Response.ok(CourseDTO.from(courseBean.find(code))).build();
    }
}
