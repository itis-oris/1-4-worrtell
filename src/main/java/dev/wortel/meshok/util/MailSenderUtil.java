package dev.wortel.meshok.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderUtil {

    //private final JavaMailSender sender;

    public void sendMessage(String to, String subject, String body) {
//        var message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        sender.send(message);
    }

}
