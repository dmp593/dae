package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT course FROM Course course ORDER BY course.name"
        )
})
public class Course {

    @Id
    @GeneratedValue
    private Long code;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Student> students;

    public Course() {
        this.students = new ArrayList<>();
    }

    public Course(String name) {
        this();
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (! this.students.contains(student)) {
            this.students.add(student);
        }
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }
}
