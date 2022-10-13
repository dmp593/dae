package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubjectBean subjectBean;

    public void create(String username, String password, String name, String email, Long courseCode) {
        var course = em.find(Course.class, courseCode);
        if (course == null) return;

        var student = new Student(username, password, name, email, course);
        em.persist(student);

        course.addStudent(student);
    }

    public Student find(String username) {
        return em.find(Student.class, username);
    }

    public List<Student> getAll() {
        return em.createNamedQuery("getAllStudents", Student.class).getResultList();
    }

    public void enroll(String studentUsername, Long subjectCode) {

        var student = this.find(studentUsername);
        var subject = subjectBean.find(subjectCode);

        if (! student.getCourse().equals(subject.getCourse())) return;

        student.addSubject(subject);
        subject.addStudent(student);
    }

    public void unroll(String studentUsername, Long subjectCode) {

        var student = this.find(studentUsername);
        var subject = subjectBean.find(subjectCode);

        if (! student.getCourse().equals(subject.getCourse())) return;

        subject.removeStudent(student);
        student.removeSubject(subject);
    }
}
