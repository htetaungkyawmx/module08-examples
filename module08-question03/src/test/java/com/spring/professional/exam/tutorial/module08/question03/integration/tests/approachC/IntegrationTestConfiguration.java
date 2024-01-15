package com.spring.professional.exam.tutorial.module08.question03.integration.tests.approachC;

import com.spring.professional.exam.tutorial.module08.question03.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.MockitoConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.TestDataConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ContextConfiguration(classes = {TestDataConfiguration.class, MockitoConfiguration.class})
public class IntegrationTestConfiguration extends ApplicationConfiguration {
}
