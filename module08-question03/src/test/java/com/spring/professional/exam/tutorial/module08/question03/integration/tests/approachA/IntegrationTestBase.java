package com.spring.professional.exam.tutorial.module08.question03.integration.tests.approachA;

import com.spring.professional.exam.tutorial.module08.question03.configuration.ApplicationConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.MockitoConfiguration;
import com.spring.professional.exam.tutorial.module08.question03.integration.tests.configuration.TestDataConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationConfiguration.class, TestDataConfiguration.class, MockitoConfiguration.class})
public abstract class IntegrationTestBase {
}
