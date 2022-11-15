package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;

@Stateless
public class EmailBean {

    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;

    public void send(String to, String subject, String text) throws MessagingException {
        var msg = new MimeMessage(mailSession);

        // Adjust the recipients. Here we have only one recipient.
        // The recipient's address must be an object of the InternetAddress class.
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

        // Set the message's subject
        msg.setSubject(subject);

        // Insert the message's body
        msg.setText(text);

        // Adjust the date of sending the message
        var timeStamp = new Date();
        msg.setSentDate(timeStamp);

        // Use the 'send' static method of the Transport class to send the message
        Transport.send(msg);
    }
}
