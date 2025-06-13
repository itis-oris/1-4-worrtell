package dev.wortel.meshok.service;

import dev.wortel.meshok.util.MailSenderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailConfirmService implements ConfirmService {

    private final MailSenderUtil mailSender;

    @Override
    public void confirm(String username, String email) {
        mailSender.sendMessage(
                email, "Подтверждение учетной записи", "Для подтверждения учетной записи ..."
        );

        // TODO

    }

}
