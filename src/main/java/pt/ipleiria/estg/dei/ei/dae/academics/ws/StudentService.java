package pt.ipleiria.estg.dei.ei.dae.academics.ws;


import pt.ipleiria.estg.dei.ei.dae.academics.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.PaginatedDTOs;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.StudentNotInTheSameSubjectCourseException;
import pt.ipleiria.estg.dei.ei.dae.academics.requests.PageRequest;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Authorized;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("students")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"Teacher"})
public class StudentService {

    @EJB
    private StudentBean studentBean;

    @EJB
    private EmailBean emailBean;

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
    @Authorized
    @RolesAllowed({"Student"})
    @Path("{username}")
    public Response get(@PathParam("username") String username) {
        return Response.ok(StudentDTO.from(studentBean.findOrFail(username))).build();
    }


    @GET
    @Path("{username}/subjects")
    public Response enrolled(@PathParam("username") String username) {
        return Response.ok(SubjectDTO.from(studentBean.enrolled(username))).build();
    }

    @GET
    @Path("{username}/subjects/unrolled")
    public Response unrolled(@PathParam("username") String username) {
        return Response.ok(SubjectDTO.from(studentBean.unrolled(username))).build();
    }

    @PATCH
    @Path("{username}/subjects/{code}/enroll")
    public Response enroll(@PathParam("username") String studentUsername, @PathParam("code") Long subjectCode) throws StudentNotInTheSameSubjectCourseException {
        studentBean.enroll(studentUsername, subjectCode);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{username}/subjects/{code}/unroll")
    public Response unroll(@PathParam("username") String studentUsername, @PathParam("code") Long subjectCode) throws StudentNotInTheSameSubjectCourseException {
        studentBean.unroll(studentUsername, subjectCode);
        return Response.noContent().build();
    }

    @POST
    @Path("/{username}/email/send")
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) throws MessagingException {
        Student student = studentBean.findOrFail(username);

        emailBean.send(student.getEmail(), email.getSubject(), email.getMessage());
        return Response.noContent().build();
    }

    @POST
    @Path("/")
    public Response create(StudentDTO studentDTO) {
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
