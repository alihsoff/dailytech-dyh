package main.dailytech.services;

import main.dailytech.dto.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    private final JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String senderMail;

    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void processMailRequest(Contact contact) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            message.setFrom(new InternetAddress(senderMail, contact.getName()));
            String body = "Name: "+contact.getName()+"<br>";
            body+="Mail: "+contact.getEmail()+"<br>";
            body+="Subject: "+contact.getSubject()+"<br>";
            body+="Message: "+contact.getMessage()+"<br>";
            helper.setText(body, true);
            helper.setSubject(contact.getSubject());
            helper.setTo(senderMail);
        } catch (MessagingException e) {
            System.out.println(e);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        sender.send(message);
    }

}
