package ru.vtkachenko.simpletmsback.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource errorMessageSource() {
        ReloadableResourceBundleMessageSource errorMessageSource = new ReloadableResourceBundleMessageSource();
        errorMessageSource.setBasename("classpath:/messages/error_messages");
        errorMessageSource.setDefaultEncoding("UTF-8");
        return errorMessageSource;
    }

    @Bean
    public MessageSource defaultMessageSource() {
        ReloadableResourceBundleMessageSource defaultMessageSource = new ReloadableResourceBundleMessageSource();
        defaultMessageSource.setBasename("classpath:/messages/messages");
        defaultMessageSource.setDefaultEncoding("UTF-8");
        return defaultMessageSource;
    }
}
