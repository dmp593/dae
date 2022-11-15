package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ErrorDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.StudentNotInTheSameSubjectCourseException;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class StudentNotInTheSameSubjectCourseExceptionMapper implements ExceptionMapper<StudentNotInTheSameSubjectCourseException> {
    @EJB
    private StudentBean studentBean;

    @EJB
    private SubjectBean subjectBean;

    @Override
    public Response toResponse(StudentNotInTheSameSubjectCourseException e) {
        var student = studentBean.findOrFail(e.getStudentUsername());
        var subject = subjectBean.findOrFail(e.getSubjectCode());

        var error = new ErrorDTO(
                Student.class.getSimpleName() + " '" + student.getName() + "'" +
                " can't enroll in " +
                Subject.class.getSimpleName() + " '" + subject.getName() + "'. " +
                "Different course."
        );

        return Response.status(Response.Status.FORBIDDEN).entity(error).build();
    }
}
