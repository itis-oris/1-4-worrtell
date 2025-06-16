package dev.wortel.meshok;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/")
                .setViewName("redirect:/items");
        registry.addViewController("/{path:[a-zA-Z0-9-]+}")
                .setViewName("redirect:/items");
        registry.addViewController("/{path:[a-zA-Z0-9-]+}/{subpath:[a-zA-Z0-9-]+}")
                .setViewName("redirect:/items");
        registry.addViewController("/{path:[a-zA-Z0-9-]+}/**")
                .setViewName("redirect:/items");
    }
}