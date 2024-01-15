package com.spring.professional.exam.tutorial.module08.question01.ds;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.Assert.*;

// This is a simple unit-test for Hibernate Data Object.
// In real projects you should identify test strategy right for you
// and you should decide if unit-tests for simple Data Objects that do not contain business logic
// makes sense. In some cases it makes more sense to test simple Data Objects only on integration level
// instead of having dedicated unit-test for each
public class GuestTest {

    private static final String JOHN = "John";
    private static final String DOE = "Doe";
    private static final UUID GUEST_ID = UUID.randomUUID();

    @Test
    public void shouldCreateGuestWithFirstNameAndLastName() {
        Guest guest = new Guest(JOHN, DOE);

        assertNull(guest.getId());
        assertSame(JOHN, guest.getFirstName());
        assertSame(DOE, guest.getLastName());
    }

    // Test below uses Spring ReflectionTestUtils to set private field ID within Guest Data Object
    // ID field will be set by reflection in production code with Hibernate
    //
    // Within Unit Test there are three options to test such a case:
    // 1) avoid such a testing at all on unit level, and test it on integration test level
    // 2) make field package-private and set it without usage of reflection
    // 3) use ReflectionTestUtils
    //
    // Personally I prefer solutions 1) and 2), this test was implemented just to show usage of ReflectionTestUtils.
    // Usage of reflection in tests is tricky, since it limits future refactorings, since you need to remember to change
    // also strings representing fields under setField, compile-time-feedback will not warn you about missing fields.
    // Although modern IDE during refactorings also perform text-based-search, compile-time-feedback is still most
    // helpful tool in large scale projects.
    //
    // ReflectionTestUtils is usually used for:
    // 1) ORM Related Unit Testing
    // 2) @Autowired, @Inject, @Resource manual injection in Unit Testing
    // 3) @PostConstruct, @PreDestroy method execution if they are private
    // Keep in mind that each case above can be also achieved with package-private access modifier,
    // this breaks encapsulation, but keeps compile-time-feedback at satisfying level
    @Test
    public void shouldSetGuestId() {
        Guest guest = new Guest();

        ReflectionTestUtils.setField(guest, "id", GUEST_ID, UUID.class);

        assertEquals(GUEST_ID, guest.getId());
    }
}