package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.StudentNotInTheSameSubjectCourseException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Stateless
public class StudentBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CourseBean courseBean;

    @EJB
    private SubjectBean subjectBean;

    public void create(String username, String password, String name, String email, Long courseCode) {
        var course = courseBean.findOrFail(courseCode);
        var student = new Student(username, password, name, email, course);

        course.addStudent(student);
        em.persist(student);
    }

    public void update(String username, String password, String name, String email, Long courseCode) {
        var student = findOrFail(username);

        em.lock(student, LockModeType.OPTIMISTIC);

        student.setPassword(password);
        student.setName(name);
        student.setEmail(email);

        // a "lazy way" that avoids querying the course every time we do an update to the student
        if (!Objects.equals(student.getCourse().getCode(), courseCode)) {
            student.setCourse(courseBean.findOrFail(courseCode));
        }
    }

    public Student find(String username) {
        return em.find(Student.class, username);
    }

    public Student findOrFail(String username) {
        var student = em.getReference(Student.class, username);
        Hibernate.initialize(student);

        return student;
    }

    public List<Student> getAll(int offset, int limit) {
        return em.createNamedQuery("getAllStudents", Student.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("SELECT COUNT(*) FROM " + Student.class.getSimpleName(), Long.class).getSingleResult();
    }

    public void enroll(String studentUsername, Long subjectCode) throws StudentNotInTheSameSubjectCourseException {
        var student = findOrFail(studentUsername);
        var subject = subjectBean.findOrFail(subjectCode);

        if (! student.getCourse().equals(subject.getCourse())) {
            throw new StudentNotInTheSameSubjectCourseException(studentUsername, subjectCode);
        }

        student.addSubject(subject);
        subject.addStudent(student);
    }

    public void unroll(String studentUsername, Long subjectCode) throws StudentNotInTheSameSubjectCourseException {

        var student = findOrFail(studentUsername);
        var subject = subjectBean.findOrFail(subjectCode);

        if (! student.getCourse().equals(subject.getCourse())) {
            throw new StudentNotInTheSameSubjectCourseException(studentUsername, subjectCode);
        }

        subject.removeStudent(student);
        student.removeSubject(subject);
    }

    public List<Subject> enrolled(String username) {
        var subjects = findOrFail(username).getSubjects();
        Hibernate.initialize(subjects);

        return subjects;
    }

    public List<Subject> unrolled(String username) {
        var student = findOrFail(username);

        return em.createNamedQuery("getAllSubjectsUnrolled", Subject.class)
            .setParameter("username", username)
            .setParameter("courseCode", student.getCourse().getCode())
            .getResultList();
    }

    public boolean isValid(String username, String password) {
        var student = find(username);

        return student != null && student.getPassword().equals(password);
    }
}
