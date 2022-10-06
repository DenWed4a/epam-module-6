package com.epam.esm.localization;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocalResourceManager {
    private ResourceBundleMessageSource resourceBundleMessageSource;

    public LocalResourceManager(ResourceBundleMessageSource resourceBundleMessageSource){
        this.resourceBundleMessageSource = resourceBundleMessageSource;
    }

    public String getMessage(String code){
        Locale locale = LocaleContextHolder.getLocale();
        return resourceBundleMessageSource.getMessage(code, null, locale);
    }

}
