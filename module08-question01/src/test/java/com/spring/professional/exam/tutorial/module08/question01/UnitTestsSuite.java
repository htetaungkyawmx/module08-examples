package com.spring.professional.exam.tutorial.module08.question01;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.runner.RunWith;
//Tests run: 28, Failures: 0, Errors: 0, Skipped: 0 11.521s
//Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 34.314 s
@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*Test.class", "!**/*IntegrationTest.class"})
public class UnitTestsSuite {
}