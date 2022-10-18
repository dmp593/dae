package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Stateless
public class CourseBean {

    @PersistenceContext
    private EntityManager em;

    public void create(Long code, String name) {
        var course = new Course(code, name);
        em.persist(course);
    }

    public List<Course> getAll(int offset, int limit) {
        return em.createNamedQuery("getAllCourses", Course.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(*) FROM " + Course.class.getSimpleName(), Long.class).getSingleResult();
    }

    public Course find(Long courseCode) {
        return em.find(Course.class, courseCode);
    }
}
