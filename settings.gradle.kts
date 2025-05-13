pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        libs {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "The Coffee App"
include(":app")
