package dev.wortel.meshok.security.ott;

import dev.wortel.meshok.entity.User;
import dev.wortel.meshok.repository.UserRepository;
import dev.wortel.meshok.util.MailSenderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OneTimeTokenGenerationSuccessHandlerMailSenderImpl implements OneTimeTokenGenerationSuccessHandler {

    private final UserRepository userRepository;
    private final MailSenderUtil mailSender;

    @Value("${server.url}")
    private String host;

    private RedirectOneTimeTokenGenerationSuccessHandler delegate = new RedirectOneTimeTokenGenerationSuccessHandler("/");

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
            throws IOException {

        Optional<User> userOptional = userRepository.findByEmail(oneTimeToken.getUsername());

        if (userOptional.isPresent()) {
            String email = userOptional.get().getEmail();
            var finalUrl = host + "/login/ott?token=" + oneTimeToken.getTokenValue();
            mailSender.sendMessage(email, "", """
                                          Ссылка для аутентификации : %s
                                          """.formatted(finalUrl));
        }

        delegate.handle(request, response, oneTimeToken);
    }
}