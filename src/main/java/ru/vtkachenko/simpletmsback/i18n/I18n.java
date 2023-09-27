package ru.vtkachenko.simpletmsback.i18n;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class I18n {
    private final MessageSource errorMessageSource;
    private final MessageSource defaultMessageSource;

    @Autowired
    I18n(MessageSource errorMessageSource, MessageSource defaultMessageSource) {
        this.errorMessageSource = errorMessageSource;
        this.defaultMessageSource = defaultMessageSource;
    }

    public String translate (@Nonnull String messageCode) {
        return translate(messageCode, null, null);
    }

    public String translate (@Nonnull String messageCode, @Nonnull Object[] args) {
        return translate(messageCode, args, null);
    }

    public String translate (@Nonnull String messageCode, @Nonnull I18nPackage i18nPackage) {
        return translate(messageCode, null, i18nPackage);
    }

    public String translate (@Nonnull String messageCode, @Nullable Object[] args, @Nullable I18nPackage i18nPackage) {
        Locale locale = LocaleContextHolder.getLocale();
        if (i18nPackage == null) {
            i18nPackage = I18nPackage.DEFAULT;
        }
        try {
            switch (i18nPackage) {
                case ERROR -> {
                    return errorMessageSource.getMessage(messageCode, null, locale);
                }
                default -> {
                    return defaultMessageSource.getMessage(messageCode, null, locale);
                }
            }
        } catch (NoSuchMessageException e) {
            log.warn("Can't found message - [{}], in package - [{}]", messageCode, i18nPackage);
        }
        return messageCode;
    }
}
