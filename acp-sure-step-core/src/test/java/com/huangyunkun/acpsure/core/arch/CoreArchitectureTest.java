package com.huangyunkun.acpsure.core.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class CoreArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter().importPackages("com.huangyunkun.acpsure.core");
    }

    @Test
    void concreteNodeClassesShouldExtendBaseNodeComponent() {
        ArchRule rule = classes()
                .that().resideInAPackage("..node..")
                .and().haveSimpleNameNotContaining("Test")
                .should().beAssignableTo(
                        com.huangyunkun.acpsure.core.node.BaseNodeComponent.class)
                .orShould().beAssignableTo(
                        com.huangyunkun.acpsure.core.node.BaseBooleanNodeComponent.class)
                .because("All node components must extend BaseNodeComponent or BaseBooleanNodeComponent");

        rule.check(importedClasses);
    }

    @Test
    void utilClassesShouldHavePrivateConstructors() {
        ArchRule rule = classes()
                .that().resideInAPackage("..util..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .and().areNotAnnotatedWith(org.springframework.stereotype.Component.class)
                .should().haveOnlyPrivateConstructors()
                .because("Utility classes should not be instantiated and must have only private constructors");

        rule.check(importedClasses);
    }

    @Test
    void utilClassesShouldHaveOnlyStaticMethods() {
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..util..")
                .and().areDeclaredInClassesThat().areNotInterfaces()
                .and().areDeclaredInClassesThat().areNotEnums()
                .and().areDeclaredInClassesThat().areNotAnnotatedWith(
                        org.springframework.stereotype.Component.class)
                .and().arePublic()
                .should().beStatic()
                .because("Utility classes should only expose static methods");

        rule.check(importedClasses);
    }

    @Test
    void packagesShouldBeFreeOfCycles() {
        ArchRule rule = slices()
                .matching("com.huangyunkun.acpsure.core.(*)..")
                .should().beFreeOfCycles()
                .because("Packages in the core module should not have cyclic dependencies");

        rule.check(importedClasses);
    }
}
