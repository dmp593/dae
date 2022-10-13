package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private StudentBean studentBean;

    @EJB
    private CourseBean courseBean;

    @EJB
    private SubjectBean subjectBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        courseBean.create(9119L, "EI");

        subjectBean.create(1L, "Desenvolvimento de Aplicações Empresariais", "2017/18", "2021/22", 9119L);

        studentBean.create("foo", "bar", "foo", "foo@bar.com", 9119L);

        studentBean.enroll("foo", 1L);
    }

}
