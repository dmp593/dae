package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.CourseDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.academics.requests.PageRequest;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.CourseBean;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authenticated;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

        var courses = courseBean.all(pageRequest.getOffset(), pageRequest.getLimit());

        var paginatedDTO = new PaginatedDTOs<>(CourseDTO.from(courses), count, pageRequest.getOffset());
        return Response.ok(paginatedDTO).build();
    }

    @GET
    @Path("{code}")
    public Response get(@PathParam("code") Long code) {
        return Response.ok(CourseDTO.from(courseBean.find(code))).build();
    }

    @POST
    @Path("")
    @Authenticated
    @RolesAllowed({"Administrator"})
    public Response create(CourseDTO course) {
        courseBean.create(course.getCode(), course.getName());

        var dto = CourseDTO.from(courseBean.find(course.getCode()));
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @PUT
    @Path("{code}")
    public Response update(@PathParam("code") Long code, CourseDTO courseDTO) {
        courseBean.update(code, courseDTO.getName());
        courseDTO = CourseDTO.from(courseBean.find(courseDTO.getCode()));

        return Response.ok(courseDTO).build();
    }

    @DELETE
    @Path("{code}")
    public Response delete(@PathParam("code") Long code) {
        courseBean.remove(code);
        return Response.noContent().build();
    }
}
