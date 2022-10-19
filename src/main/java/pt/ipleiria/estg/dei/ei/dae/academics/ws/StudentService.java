package pt.ipleiria.estg.dei.ei.dae.academics.ws;


import pt.ipleiria.estg.dei.ei.dae.academics.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.requests.PageRequest;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("students")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StudentService {

    @EJB
    private StudentBean studentBean;

    @GET
    @Path("/")
    public Response all(@BeanParam @Valid PageRequest pageRequest) {
        var count = studentBean.count();

        if (pageRequest.getOffset() > count) {
            return Response.ok(new PaginatedDTOs<>(count)).build();
        }

        var students = studentBean.getAll(pageRequest.getOffset(), pageRequest.getLimit());
        var paginatedDTO = new PaginatedDTOs<>(StudentDTO.from(students), count, pageRequest.getOffset());

        return Response.ok(paginatedDTO).build();
    }

    @GET
    @Path("{username}")
    public Response get(@PathParam("username") String username) {
        Student student = studentBean.find(username);

        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("STUDENT_NOT_FOUND").build();
        }

        return Response.ok(StudentDTO.from(student)).build();
    }


    @GET
    @Path("{username}/subjects")
    public Response subjects(@PathParam("username") String username) {
        Student student = studentBean.find(username);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("STUDENT_NOT_FOUND").build();
        }

        return Response.ok(SubjectDTO.from(student.getSubjects())).build();
    }


    @POST
    @Path("/")
    public Response create(StudentDTO studentDTO) throws MyConstraintViolationException {
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        var student = studentBean.find(studentDTO.getUsername());
        return Response.status(Response.Status.CREATED).entity(StudentDTO.from(student)).build();
    }
}
