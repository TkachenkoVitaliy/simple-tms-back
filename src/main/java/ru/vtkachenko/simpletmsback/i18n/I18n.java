package ru.vtkachenko.simpletmsback.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18n {
    private final MessageSource messageSource;

    @Autowired
    I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(String messageCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, null, locale);
    }

}
