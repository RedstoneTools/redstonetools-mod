plugins {
	id("maven-publish")
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
	id("dev.kikugie.loom-back-compat")
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = project.property("maven_group")!!

val lastTask: Task = (if (sc.current.parsed.matches("<26.1")) tasks.named<Task>("remapJar") else tasks.jar).get()

base {
	archivesName.set(project.property("archives_base_name") as String)
}

loom {
	splitEnvironmentSourceSets()

	mods {
		create("redstonetools", Action {
			sourceSet(sourceSets.main.get())
			sourceSet("client")
		})
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
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	if (sc.current.parsed.matches("<26.1")) {
		mappings(loom.officialMojangMappings())
		modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
		modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
		modImplementation("com.sk89q.worldedit:worldedit-fabric-mc${project.property("worldedit_version")}")
		modImplementation("fi.dy.masa.malilib:malilib-fabric-${project.property("malilib_version")}")
		modImplementation("net.kr1v:malilib-api:${project.property("malilib_api_version")}") {
			exclude(group = "net.fabricmc.fabric-api") // prevent 1.21.5 fabric api modules used by malilib from leaking into 1.21.4
		}
	}  else {
		implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
		implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
		implementation("com.sk89q.worldedit:worldedit-fabric-mc${project.property("worldedit_version")}")
		implementation("fi.dy.masa.malilib:malilib-fabric-${project.property("malilib_version")}")
		implementation("net.kr1v:malilib-api:${project.property("malilib_api_version")}")
	}
	annotationProcessor("net.kr1v:malilib-api-processor:1.0.0")
}

configurations.all {
	resolutionStrategy {
		force("com.google.code.gson:gson:2.13.2")
	}
}

loom {
	decompilerOptions.named("vineflower") {
		options.put("mark-corresponding-synthetics", "1")
	}

	runConfigs.configureEach {
		ideConfigGenerated(true)
		runDir = "../../run"
	}
}

tasks.processResources {
	val properties = mapOf(
		"version" to project.version,
		"minecraft_version" to project.property("minecraft_version"),
		"minecraft_version_out" to project.property("minecraft_version_out"),
		"malilib_version" to project.property("minecraft_version_out"),
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

	from(lastTask)
	into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod_version")}"))
}

tasks.register<DefaultTask>("buildAndCollect") {
	group = "build"
	dependsOn(tasks.named("build"), tasks.named("collectFile"))
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	if (sc.current.parsed.matches("<26.1"))
		options.release = 21
	else
		options.release = 25
}

java {
	withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    if (sc.current.parsed.matches("<26.1"))
        options.release = 21
    else
        options.release = 25
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
	file.set((lastTask as AbstractArchiveTask).archiveFile)
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
