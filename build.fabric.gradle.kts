import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
	id("maven-publish")
	id("me.modmuss50.mod-publish-plugin") version "2.2.1"
	id("dev.kikugie.loom-back-compat")
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = project.property("maven_group")!!

val requiredJava: JavaVersion = when {
	sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
	sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
	sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
	sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
	else -> JavaVersion.VERSION_1_8
}

base {
	archivesName.set(project.property("archives_base_name") as String)
}

loom {
	splitEnvironmentSourceSets()

	mods {
		create("redstonetools") {
			sourceSet(sourceSets.main.get())
			sourceSet("client")
		}
	}
}

stonecutter {
	swaps["mouse_clicked_params"] = if (current.parsed < "1.21.10") "double mouseX, double mouseY, int button" else "MouseButtonEvent click, boolean doubleClick" // mouseClicked
	swaps["mouse_clicked_args"] = if (current.parsed < "1.21.10") "mouseX, mouseY, button" else "click, doubleClick"
	swaps["on_mouse_clicked_args"] = if (current.parsed < "1.21.10") "(int) mouseX, (int) mouseY, button" else "click, doubleClick" // onMouseClicked
	swaps["dragged_released_params"] = if (current.parsed < "1.21.10") "double mouseX, double mouseY, int button" else "MouseButtonEvent click" // mouseDragged, mouseReleased
	swaps["dragged_released_args"] = if (current.parsed < "1.21.10") "mouseX, mouseY, button" else "click"
	swaps["on_released_args"] = if (current.parsed < "1.21.10") "(int) mouseX, (int) mouseY, button" else "click" // onMouseReleased
	swaps["keyinput_params"] = if (current.parsed < "1.21.10") "int keyCode, int scanCode, int modifiers" else "KeyEvent input" // keyPressed, keyReleased
	swaps["keyinput_args"] = if (current.parsed < "1.21.10") "keyCode, scanCode, modifiers" else "input"
	swaps["charinput_params"] = if (current.parsed < "1.21.10") "char chr, int modifiers" else "CharacterEvent input" // charTyped
	swaps["charinput_args"] = if (current.parsed < "1.21.10") "chr, modifiers" else "input"
	swaps["click_and_inputs_imports"] = if (current.parsed < "1.21.10") "" else
		"//\nimport net.minecraft.client.gui.Click;\nimport net.minecraft.client.input.KeyInput;\nimport net.minecraft.client.input.CharInput;"

}

repositories {
	mavenCentral()
	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
	maven {
		name = "WorldEdit Maven"
		url = uri("https://maven.enginehub.org/repo/")
	}
	maven {
		name = "kr1v"
		url = uri("https://repo.repsy.io/kr1v/maven/")
	}
	maven {
		name = "Sakura Ryoko"
		url = uri("https://masa.dy.fi/maven/sakura-ryoko/")
	}
	maven {
		name = "Fallen Breath"
		url = uri("https://maven.fallenbreath.me/releases/")
	}
}

dependencies {
	components.withModule("com.sk89q.worldedit:worldedit-core") {
		allVariants {
			attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 21)
		}
	}

	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

	loomx.applyMojangMappings()
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
	modImplementation("com.sk89q.worldedit:worldedit-fabric-mc${project.property("worldedit_version")}")
	modImplementation("fi.dy.masa.malilib:malilib-fabric-${project.property("malilib_version")}")
	modImplementation("net.kr1v:malilib-api:${project.property("malilib_api_version")}-${project.property("malilib_api_mc_version")}") {
		exclude(group = "net.fabricmc.fabric-api") // prevent 1.21.5 fabric api modules used by malilib from leaking into 1.21.4
		exclude(group = "fi.dy.masa.malilib")
	}

	"clientAnnotationProcessor"("net.kr1v:malilib-api-processor:1.0.0")
}

configurations.all {
	resolutionStrategy {
		force("com.google.code.gson:gson:2.13.2")
		if (project.name == "26.1.2-fabric") {
			force("com.sk89q.worldedit:worldedit-core:8.0.0-20260509.035034-8")
		}
	}
}

loom {
	decompilerOptions.named("vineflower") {
		options.put("mark-corresponding-synthetics", "1")
	}

	runConfigs.configureEach {
		generateRunConfig = true
		runDirectory.dir("../../run")
	}
}

tasks.processResources {
	val properties = mapOf(
		"version" to project.version,
		"minecraft_version" to project.property("minecraft_version"),
		"loader_version" to project.property("loader_version")
	)
	properties.forEach { inputs.property(it.key, it.value) }
	filteringCharset = "UTF-8"

	filesMatching("fabric.mod.json") {
		expand(properties)
	}
}

tasks.register<Copy>("collectFile") {
	group = "build"

	from(loomx.modJar)
	into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod_version")}"))
}

tasks.register<DefaultTask>("buildAndCollect") {
	group = "build"
	dependsOn(tasks.named("build"), tasks.named("collectFile"))
}

java {
	targetCompatibility = requiredJava
	sourceCompatibility = requiredJava
	toolchain.languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)

	withSourcesJar()
}

tasks.jar {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
	from("LICENSE") {
		rename { "${it}_${project.property("archives_base_name")}" }
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = project.property("archives_base_name") as String
			from(components["java"])
		}
	}
	repositories {
	}
}

publishMods {
	file.set(loomx.modJar.get().archiveFile)
	type.set(STABLE)
	modLoaders.add("fabric")

	changelog.set("")
	displayName.set(project.property("mod_version") as String)

	if (providers.environmentVariable("RELEASE_MODRINTH").orNull?.toBoolean() ?: false) {
		modrinth {
			accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
			projectId.set("9ySQVrz2")
			minecraftVersions.add(project.property("minecraft_version") as String)

			requires("fabric-api")
			requires("malilib")
			requires("malilib-api")
			optional("worldedit")
		}
	}

	if (providers.environmentVariable("RELEASE_GITHUB").orNull?.toBoolean() ?: false) {
		github {
			accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
			parent(rootProject.tasks.named("publishGithub"))
		}
	}
}
