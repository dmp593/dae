package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
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

    public boolean exists(String username) {
        var query = em.createQuery("SELECT COUNT(s.username) FROM Student s WHERE s.username = :username", Long.class);
        query.setParameter("username", username);
        return query.getFirstResult() > 0;
    }

    public void create(String username, String password, String name, String email, Long courseCode)
        throws MyConstraintViolationException {
        if (exists(username)) {
            throw new MyEntityExistsException("Student with username '" + username + "' already exists");
        }

        var course = courseBean.find(courseCode);
        if (course == null) {
            throw new MyEntityNotFoundException("Course with code '" + courseCode + "' not found");
        }

        try {
            var student = new Student(username, password, name, email, course);
            course.addStudent(student);

            em.persist(student);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email, Long courseCode) {
        var student = em.find(Student.class, username);

        if (student == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + username);
            return;
        }

        em.lock(student, LockModeType.OPTIMISTIC);

        student.setPassword(password);
        student.setName(name);
        student.setEmail(email);

        // a "lazy way" that avoids querying the course every time we do an update to the student
        if (!Objects.equals(student.getCourse().getCode(), courseCode)) {
            var course = em.find(Course.class, courseCode);

            if (course == null) {
                System.err.println("ERROR_COURSE_NOT_FOUND: " + courseCode);
                return;
            }

            student.setCourse(course);
        }
    }

    public Student find(String username) {
        return em.find(Student.class, username);
    }

    public List<Student> getAll(int offset, int limit) {
        return em.createNamedQuery("getAllStudents", Student.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(*) FROM " + Student.class.getSimpleName(), Long.class).getSingleResult();
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
