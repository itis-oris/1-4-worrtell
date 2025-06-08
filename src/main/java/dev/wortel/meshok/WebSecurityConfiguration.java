package dev.wortel.meshok;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/hello").hasRole("ADMIN")
//                        .requestMatchers("/registration").anonymous()
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults());
//                .oneTimeTokenLogin(Customizer.withDefaults())
//                .rememberMe(Customizer.withDefaults())
//                .oauth2Login(Customizer.withDefaults());

        return httpSecurity.build();
    }

}
