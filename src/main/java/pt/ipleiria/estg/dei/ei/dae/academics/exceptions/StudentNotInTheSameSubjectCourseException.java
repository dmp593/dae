package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

public class StudentNotInTheSameSubjectCourseException extends Exception {

    private String studentUsername;
    private Long subjectCode;

    public StudentNotInTheSameSubjectCourseException(String studentUsername, Long subjectCode) {
        super(
            Student.class.getSimpleName() + " '" + studentUsername + "'" +
            " does not have permission to enroll in the "
            + Subject.class.getSimpleName() + " '" + subjectCode + "'"
        );

        this.studentUsername = studentUsername;
        this.subjectCode = subjectCode;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public Long getSubjectCode() {
        return subjectCode;
    }
}
