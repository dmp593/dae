package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CourseBean {

    @PersistenceContext
    private EntityManager em;

    public void create(Long code, String name) {
        var course = new Course(code, name);
        em.persist(course);
    }

    public List<Course> all(int offset, int limit) {
        return em.createNamedQuery("getAllCourses", Course.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("SELECT COUNT(*) FROM " + Course.class.getSimpleName(), Long.class).getSingleResult();
    }

    public Course find(Long code) {
        return em.find(Course.class, code);
    }

    public Course findOrFail(Long code) {
        var course = em.getReference(Course.class, code);
        Hibernate.initialize(course);

        return course;
    }

    public void update(Long code, String name) {
        var course = findOrFail(code);
        course.setName(name);
        em.merge(course);
    }

    // if for some reason you need to change the PK
    // Hibernate doesn't allow that modification.
    // The workaround is to delete the old one (be careful with cascade operations)
    // and create a new one.
    public void update(Long oldCode, Long newCode, String name) {
        var oldCourse = findOrFail(oldCode);

        create(newCode, name + ".tmp"); // unique key on column name
        var newCourse = findOrFail(newCode);

        while (oldCourse.getStudents().size() > 0) {
            var student = oldCourse.getStudents().get(0);
            student.setCourse(newCourse);

            oldCourse.removeStudent(student);
            newCourse.addStudent(student);
        }

        while (oldCourse.getSubjects().size() > 0) {
            var subject = oldCourse.getSubjects().get(0);
            subject.setCourse(newCourse);

            oldCourse.removeSubject(subject);
            newCourse.addSubject(subject);
        }

        em.flush();

        em.remove(oldCourse);
        em.flush();

        newCourse.setName(name);
        em.merge(newCourse);
    }

    public void remove(Long code) {
        em.remove(findOrFail(code));
    }
}
