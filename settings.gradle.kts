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
		maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
	id("dev.kikugie.loom-back-compat") version "0.4"
}

rootProject.name = "redstonetools-mod"

stonecutter {
    centralScript = "build.gradle.kts"

    create(rootProject) {
		fun match(project: String, vararg loaders: String, version: String = project) {
			for (loader in loaders) version("$project-$loader", version).buildscript("build.$loader.gradle.kts")
		}

		match("1.21.4", "fabric")
		match("1.21.5", "fabric")
		match("1.21.8", "fabric", "paper")
		match("1.21.10", "fabric")
		match("1.21.11", "fabric")
		match("26.1.2", "fabric", "paper")
		match("26.2", "fabric")
		match("26.3", "fabric")

		vcsVersion = "1.21.11-fabric"
    }
}
