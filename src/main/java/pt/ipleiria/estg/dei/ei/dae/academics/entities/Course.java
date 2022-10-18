package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT course FROM Course course ORDER BY course.name"
        )
})
public class Course extends Versionable {

    @Id
    private Long code;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Student> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<Subject> subjects;

    public Course() {
        this.students = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    public Course(Long code, String name) {
        this();
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addStudent(Student student) {
        if (! this.students.contains(student)) {
            this.students.add(student);
        }
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public void addSubject(Subject subject) {
        if (! this.subjects.contains(subject)) {
            this.subjects.add(subject);
        }
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Course && Objects.equals(((Course) obj).code, this.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
