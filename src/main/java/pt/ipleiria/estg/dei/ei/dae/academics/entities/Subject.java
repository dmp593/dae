package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "subjects",
        uniqueConstraints = @UniqueConstraint(columnNames = { "name", "course_code", "scholar_year" })
)
@NamedQueries({
        @NamedQuery(
                name = "getAllSubjects",
                query = "SELECT s FROM Subject s ORDER BY s.course.name, s.scholarYear DESC, s.courseYear, s.name"
        )
})
public class Subject extends Versionable {

    @Id
    private Long code;

    private String name;

    @Column(name = "course_year")
    private String courseYear;

    @Column(name = "scholar_year")
    private String scholarYear;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects_students",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "student_username", referencedColumnName = "username")
    )
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "subjects_teachers",
            joinColumns = @JoinColumn(name = "subject_code", referencedColumnName = "code"),
            inverseJoinColumns = @JoinColumn(name = "teacher_username", referencedColumnName = "username")
    )
    private List<Teacher> teachers;

    public Subject() {
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public Subject(Long code, String name, String courseYear, String scholarYear, Course course) {
        this();
        this.code = code;
        this.name = name;
        this.courseYear = courseYear;
        this.scholarYear = scholarYear;
        this.course = course;
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

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getScholarYear() {
        return scholarYear;
    }

    public void setScholarYear(String scholarYear) {
        this.scholarYear = scholarYear;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void addStudent(Student student) {
        if (! this.students.contains(student)) {
            this.students.add(student);
        }
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public void addTeacher(Teacher teacher) {
        if (! this.teachers.contains(teacher)) {
            this.teachers.add(teacher);
        }
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
    }
}
