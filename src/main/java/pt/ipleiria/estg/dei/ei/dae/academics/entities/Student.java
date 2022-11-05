package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT s FROM Student s ORDER BY s.name"
        ),

        // SOLUTION 1
        @NamedQuery(
                name = "getAllSubjectsUnrolled",
                query = "SELECT s FROM Subject s WHERE s.code NOT IN" +
                        "    (SELECT ss.subjectCode FROM SubjectStudent ss WHERE ss.studentUsername = :username) " +
                        "AND s.course.code = :courseCode"
        ),

        // SOLUTION 2
        @NamedQuery(
                name = "getAllSubjectsUnrolledWithoutUsingPivotEntity",
                query = "SELECT s FROM Subject s WHERE s NOT IN " +
                        "   (SELECT s FROM Student st JOIN st.subjects s WHERE st.username = :username) " +
                        "AND s.course.code = :courseCode"
        )
})
public class Student extends User {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_code")
    @NotNull
    private Course course;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Subject> subjects;

    public Student() {
        this.subjects = new ArrayList<>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        this.subjects = new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject subject) {
        if (! this.subjects.contains(subject)) {
            this.subjects.add(subject);
        }
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
    }
}
