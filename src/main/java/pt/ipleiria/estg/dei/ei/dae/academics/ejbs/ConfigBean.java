package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.StudentNotInTheSameSubjectCourseException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private StudentBean studentBean;

    @EJB
    private CourseBean courseBean;

    @EJB
    private SubjectBean subjectBean;

    private Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB() throws StudentNotInTheSameSubjectCourseException {
        System.out.println("Hello Java EE!");

        administratorBean.create("admin", "admin", "admin", "admin@academics.pt");

        courseBean.create(9119L, "EI");
        courseBean.create(9281L, "EE");
        courseBean.create(9823L, "EM");
        courseBean.create(9822L, "EA");
        courseBean.create(9738L, "ET");

        subjectBean.create(1L, "Desenvolvimento de Aplicações Empresariais", "2017/18", "2021/22", 9119L);
        subjectBean.create(2L, "Desenvolvimento de Aplicações Distribuidas", "2017/18", "2021/22", 9119L);
        subjectBean.create(3L, "Cálculo II", "2017/18", "2021/22", 9738L);

        studentBean.create("foo", "bar", "foo", "foo@bar.com", 9119L);
        studentBean.create("student", "secret", "student", "student@my.ipleiria.pt", 9119L);

        studentBean.enroll("foo", 1L);
    }

}
