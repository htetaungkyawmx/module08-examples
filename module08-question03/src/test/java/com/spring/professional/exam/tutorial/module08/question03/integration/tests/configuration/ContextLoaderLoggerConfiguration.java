package com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ContextLoaderLoggerConfiguration implements ApplicationContextAware {

    private static int numberOfContexts;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("CONTEXT LOADER LOGGER => Application Context No " + (++numberOfContexts) +
                " was created at " + Objects.hashCode(applicationContext.hashCode()));
    }
}
