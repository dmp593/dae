package pt.ipleiria.estg.dei.ei.dae.academics.ws;


import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("students")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StudentService {

    @EJB
    private StudentBean studentBean;

    @GET
    @Path("/")
    public List<StudentDTO> getAll() {
        return StudentDTO.from(studentBean.getAll());
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
    public Response createNewStudent (StudentDTO studentDTO){
        studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
        );

        var student = studentBean.find(studentDTO.getUsername());
        if(student == null) return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED).entity(StudentDTO.from(student)).build();
    }
}
