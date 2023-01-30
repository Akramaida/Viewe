package com.project.viewe.service;


import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    @Async
    void sendMail(Email email){
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("viewe@email.com");
            messageHelper.setTo(email.getRecipient());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getBody());
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Activation email send");
        }catch (Exception exception){
            log.error("Exception occurred when sending mail ", exception);
            throw new ProjectException(" Exception occurred when sending email " + email.getRecipient(), exception);
        }
    }

}
