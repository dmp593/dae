package pt.ipleiria.estg.dei.ei.dae.academics.dtos;

import java.io.Serializable;

public class EmailDTO implements Serializable {
    private String subject;

    private String message;

    public EmailDTO() {}

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
