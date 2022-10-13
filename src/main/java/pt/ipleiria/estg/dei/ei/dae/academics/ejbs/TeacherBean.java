package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TeacherBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private SubjectBean subjectBean;

    public void create(String username, String password, String name, String email, String office) {
        var teacher = new Teacher(username, password, name, email, office);
        em.persist(teacher);
    }

    public Teacher find(String username) {
        return em.find(Teacher.class, username);
    }

    public void teach(String teacherUsername, Long subjectCode) {
        var teacher = this.find(teacherUsername);
        var subject = subjectBean.find(subjectCode);

        teacher.addSubject(subject);
        subject.addTeacher(teacher);
    }

    public void unteach(String teacherUsername, Long subjectCode) {
        var teacher = this.find(teacherUsername);
        var subject = subjectBean.find(subjectCode);

        subject.removeTeacher(teacher);
        teacher.removeSubject(subject);
    }
}
