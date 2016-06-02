package org.springframework.data.crate.core.mapping.event;

import org.springframework.context.annotation.Bean;
import org.springframework.data.crate.config.TestCrateConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


/**
 * @author Hasnain Javed
 * @since 1.0.0
 */
public class LifecycleEventConfigurationBase extends TestCrateConfiguration {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ValidatingCrateEventListener validatingCrateEventListener() {
        return new ValidatingCrateEventListener(validator());
    }

    @Bean
    public SimpleMappingEventListener simpleMappingEventListener() {
        return new SimpleMappingEventListener();
    }

    @Override
    protected String getMappingBasePackage() {
        return "org.springframework.data.crate.core.mapping.event";
    }
}
