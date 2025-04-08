
pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repo.agora.io/repo/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://artifactory.agora.io/artifactory/libs-release") } // Repositorio correcto de Agora
    }
}


rootProject.name = "LAB Dispositivos Moviles BAV"
include(":app")