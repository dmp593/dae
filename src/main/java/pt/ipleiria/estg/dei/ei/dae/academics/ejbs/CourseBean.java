package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

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

    public List<Course> getAll() {
        return em.createNamedQuery("getAllCourses", Course.class).getResultList();
    }

    public Course find(Long courseCode) {
        return em.find(Course.class, courseCode);
    }
}
