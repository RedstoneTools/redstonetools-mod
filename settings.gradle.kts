pluginManagement {
    repositories {
		maven {
			url = uri("https://maven.fabricmc.net/")
		}
        mavenCentral()
        gradlePluginPortal()

        maven {
            name = "KikuGie Snapshots"
            url = uri("https://maven.kikugie.dev/snapshots")
        }

        maven {
            name = "KikuGie Releases"
            url = uri("https://maven.kikugie.dev/releases")
        }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.3"
	id("dev.kikugie.loom-back-compat") version "0.3"
}

rootProject.name = "redstonetools-mod"

stonecutter {
    centralScript = "build.gradle.kts"

    create(rootProject) {
        versions("1.21.4", "1.21.5", "1.21.8", "1.21.10", "1.21.11", "26.1.2")
		vcsVersion = "1.21.11"
    }
}
