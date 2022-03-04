package com.pbpoints;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.pbpoints");

        noClasses()
            .that()
            .resideInAnyPackage("com.pbpoints.service..")
            .or()
            .resideInAnyPackage("com.pbpoints.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.pbpoints.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
