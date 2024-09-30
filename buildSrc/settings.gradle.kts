plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

// Source:
// https://docs.gradle.org/current/userguide/platforms.html#sec:importing-catalog-from-file
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
