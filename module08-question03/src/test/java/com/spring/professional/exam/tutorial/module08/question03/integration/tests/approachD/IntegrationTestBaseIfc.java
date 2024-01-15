package com.spring.professional.exam.tutorial.module08.question03.integration.tests.approachD;

import com.spring.professional.exam.tutorial.module08.question03.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.MockitoConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.TestDataConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ApplicationConfiguration.class, TestDataConfiguration.class, MockitoConfiguration.class})
public interface IntegrationTestBaseIfc {
}
