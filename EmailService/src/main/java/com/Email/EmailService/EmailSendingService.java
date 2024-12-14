package com.Email.EmailService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(KafkaModel model){
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(model.getSendto());
        mail.setSubject("Varification OTP");
        mail.setText(model.getOtp());
        mailSender.send(mail);
    }
}
