package com.huangyunkun.acpsure.core.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationAwareUtilTest {

    interface TestService {
        String getName();
    }

    static class TestServiceImpl implements TestService {
        private final String name;

        TestServiceImpl(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    static class TestComponent {
        private final String value;

        TestComponent(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @BeforeEach
    void setUp() {
        ApplicationAwareUtil.clear();
    }

    @AfterEach
    void tearDown() {
        ApplicationAwareUtil.clear();
    }

    @Test
    void shouldRegisterAndGetBeanByClass() {
        TestComponent component = new TestComponent("test-value");
        ApplicationAwareUtil.regBean(component);

        TestComponent retrieved = ApplicationAwareUtil.getBean(TestComponent.class);
        assertThat(retrieved.getValue(), is("test-value"));
    }

    @Test
    void shouldRegisterAndGetBeanByInterface() {
        TestService service = new TestServiceImpl("my-service");
        ApplicationAwareUtil.regBean(service, TestService.class);

        TestService retrieved = ApplicationAwareUtil.getBean(TestService.class);
        assertThat(retrieved.getName(), is("my-service"));
    }

    @Test
    void shouldThrowExceptionWhenGetNonExistentBean() {
        assertThrows(IllegalArgumentException.class, () -> {
            ApplicationAwareUtil.getBean(TestComponent.class);
        });
    }

    @Test
    void shouldThrowExceptionWhenRegisterNullBean() {
        assertThrows(IllegalArgumentException.class, () -> {
            ApplicationAwareUtil.regBean(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenRegisterBeanWithNullType() {
        TestComponent component = new TestComponent("test");
        assertThrows(IllegalArgumentException.class, () -> {
            ApplicationAwareUtil.regBean(component, null);
        });
    }

    @Test
    void shouldContainsBeanWhenRegistered() {
        TestComponent component = new TestComponent("test");
        ApplicationAwareUtil.regBean(component);

        assertTrue(ApplicationAwareUtil.containsBean(TestComponent.class));
        assertFalse(ApplicationAwareUtil.containsBean(TestService.class));
    }

    @Test
    void shouldRemoveBean() {
        TestComponent component = new TestComponent("test");
        ApplicationAwareUtil.regBean(component);

        Object removed = ApplicationAwareUtil.removeBean(TestComponent.class);
        assertThat(removed, is(component));
        assertFalse(ApplicationAwareUtil.containsBean(TestComponent.class));
    }

    @Test
    void shouldReturnNullWhenRemoveNonExistentBean() {
        Object removed = ApplicationAwareUtil.removeBean(TestComponent.class);
        assertNull(removed);
    }

    @Test
    void shouldClearAllBeans() {
        TestComponent component1 = new TestComponent("test1");
        TestService component2 = new TestServiceImpl("test2");

        ApplicationAwareUtil.regBean(component1);
        ApplicationAwareUtil.regBean(component2, TestService.class);

        ApplicationAwareUtil.clear();

        assertFalse(ApplicationAwareUtil.containsBean(TestComponent.class));
        assertFalse(ApplicationAwareUtil.containsBean(TestService.class));
    }

    @Test
    void shouldSupportMultipleBeans() {
        TestComponent component1 = new TestComponent("test1");
        TestComponent component2 = new TestComponent("test2");

        ApplicationAwareUtil.regBean(component1);
        ApplicationAwareUtil.regBean(component2, TestComponent.class);

        TestComponent retrieved = ApplicationAwareUtil.getBean(TestComponent.class);
        assertThat(retrieved.getValue(), is("test2"));
    }

    @Test
    void shouldThrowUnsupportedOperationExceptionForGetBeanWithoutClass() {
        assertThrows(UnsupportedOperationException.class, () -> {
            ApplicationAwareUtil.getBean();
        });
    }
}
