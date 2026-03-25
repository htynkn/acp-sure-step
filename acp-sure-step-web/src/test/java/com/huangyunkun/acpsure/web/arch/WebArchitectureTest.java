package com.huangyunkun.acpsure.web.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class WebArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter().importPackages("com.huangyunkun.acpsure.web");
    }

    @Test
    void controllerClassesShouldBeAnnotatedWithController() {
        ArchRule rule = classes()
                .that().resideInAPackage("..controller..")
                .and().areNotInterfaces()
                .should().beAnnotatedWith(org.springframework.stereotype.Controller.class)
                .because("All classes in the controller package should be annotated with @Controller");

        rule.check(importedClasses);
    }

    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .and().areNotInterfaces()
                .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
                .because("All classes in the service package should be annotated with @Service");

        rule.check(importedClasses);
    }

    @Test
    void aspectClassesShouldBeAnnotatedWithAspect() {
        ArchRule rule = classes()
                .that().resideInAPackage("..aspect..")
                .and().areNotInterfaces()
                .should().beAnnotatedWith(org.aspectj.lang.annotation.Aspect.class)
                .because("All classes in the aspect package should be annotated with @Aspect");

        rule.check(importedClasses);
    }

    @Test
    void packagesShouldBeFreeOfCycles() {
        ArchRule rule = slices()
                .matching("com.huangyunkun.acpsure.web.(*)..")
                .should().beFreeOfCycles()
                .because("Packages in the web module should not have cyclic dependencies");

        rule.check(importedClasses);
    }
}
