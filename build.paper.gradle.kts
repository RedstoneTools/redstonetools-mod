plugins {
	id("java-library")
	id("xyz.jpenilla.run-paper") version "3.1.0"
	id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

version = "${project.property("mod_version")}+${stonecutter.current.project}"
group = project.property("maven_group")!!

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/") {
		name = "Paper"
	}
	maven("https://maven.enginehub.org/repo/") {
		name = "WorldEdit Maven"
	}
}

base.archivesName = project.property("archives_base_name") as String

dependencies {
	if (stonecutter.current.parsed >= "26.1")
		paperweight.paperDevBundle("${project.property("minecraft_version")}.build.+")
	else
		paperweight.paperDevBundle("${project.property("minecraft_version")}-R0.1-SNAPSHOT")
	compileOnly("com.sk89q.worldedit:worldedit-bukkit:${project.property("worldedit_version")}")
}

tasks.register<Copy>("collectFile") {
	group = "build"

	from(tasks.jar.map { it.archiveFile })
	into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod_version")}"))
}

tasks.register<DefaultTask>("buildAndCollect") {
	group = "build"
	dependsOn(tasks.named("build"), tasks.named("collectFile"))
}

configurations.all {
	resolutionStrategy {
		if (project.name == "26.1.2-fabric") {
			force("com.sk89q.worldedit:worldedit-core:8.0.0-20260509.035034-8")
		}
	}
}

val requiredJava: JavaVersion = when {
	sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
	sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
	sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
	sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
	else -> JavaVersion.VERSION_1_8
}

java {
	targetCompatibility = requiredJava
	sourceCompatibility = requiredJava
	toolchain.languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)

	withSourcesJar()
}

tasks {
	runServer {
		minecraftVersion(project.property("minecraft_version") as String)
		jvmArgs("-Xms4G", "-Xmx4G", "-Dcom.mojang.eula.agree=true")
	}

	processResources {
		val props = mapOf("version" to version)
		filesMatching("plugin.yml") {
			expand(props)
		}
	}
}
