package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDTO implements Serializable {

    @NotNull
    protected String username;

    @NotNull
    protected String name;

    @NotNull
    protected String email;

    @NotNull
    protected Long courseCode;

    protected String courseName;

    public StudentDTO() {}

    public StudentDTO(String username, String name, String email, Long courseCode, String courseName) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(Long courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public static StudentDTO from(Student student) {
        return new StudentDTO(
                student.getUsername(),
                student.getName(),
                student.getEmail(),
                student.getCourse().getCode(),
                student.getCourse().getName()
        );
    }

    public static List<StudentDTO> from(List<Student> students) {
        return students.stream().map(StudentDTO::from).collect(Collectors.toList());
    }
}
