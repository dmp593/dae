package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDTO implements Serializable {

    private Long code;

    private String name;

    public CourseDTO() {
    }

    public CourseDTO(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CourseDTO from(Course course) {
        return new CourseDTO(
                course.getCode(),
                course.getName()
        );
    }

    public static List<CourseDTO> from(List<Course> students) {
        return students.stream().map(CourseDTO::from).collect(Collectors.toList());
    }
}
