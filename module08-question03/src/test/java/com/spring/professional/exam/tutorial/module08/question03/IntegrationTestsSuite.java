package com.spring.professional.exam.tutorial.module08.question03;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.runner.RunWith;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses("**/*IntegrationTest[0-9]*.class")
public class IntegrationTestsSuite {
}