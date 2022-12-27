package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Document;

@Stateless
public class DocumentBean {
    
    @EJB
    private StudentBean studentBean;

    @PersistenceContext
    private EntityManager em;
    
    public Document create(String filepath, String filename, String username) {
        var student = studentBean.findOrFail(username);
        var document = new Document(filepath, filename, student);

        em.persist(document);
        student.addDocument(document);

        return document;
    }

    public Document find(Long id) {
        return em.find(Document.class, id);
    }

    public Document findOrFail(Long id) {
        var document = em.getReference(Document.class, id);
        Hibernate.initialize(document);
        return document;
    }
    
    public List<Document> getStudentDocuments(String username){
        return em.createNamedQuery("getStudentDocuments", Document.class).setParameter("username", username).getResultList();
    }

}
