plugins {
    id("dev.kikugie.stonecutter")
	id("me.modmuss50.mod-publish-plugin") version "2.2.1"
}
stonecutter.active("26.1.2-paper")

stonecutter parameters {
	val (version, loader) = current.project.split('-', limit = 2)

	constants {
		match(loader, "fabric", "paper")
	}
}

stonecutter parameters {
	replacements.string(current.parsed >= "26.1") {
		replace(
			"GuiGraphics",
			"GuiGraphicsExtractor"
		)
	}
	replacements.string(current.parsed >= "1.21.11") {
		replace(
			"ResourceLocation",
			"Identifier"
		)
	}
	replacements.string {
		direction = true
		replace(
			"fromGuiGraphicsExtractor",
			"fromGuiGraphics"
		)
	}
}

publishMods {
	if (providers.environmentVariable("RELEASE_MODRINTH").orNull?.toBoolean() ?: false) {
		val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
		if (!modrinthToken.isPresent || modrinthToken.get() == "") {
			throw GradleException("Missing MODRINTH_TOKEN")
		}
	}

	if (providers.environmentVariable("RELEASE_GITHUB").orNull?.toBoolean() ?: false) {
		val githubToken = providers.environmentVariable("GITHUB_TOKEN")
		if (!githubToken.isPresent || githubToken.get() == "") {
			throw GradleException("Missing GITHUB_TOKEN")
		}

		github {
			accessToken = githubToken
			repository = providers.environmentVariable("GITHUB_REPO")
			commitish = "main"
			changelog = ""
			type = STABLE
			tagName = "${rootProject.property("mod_version")}"
			displayName = "Redstone Tools ${rootProject.property("mod_version")}"
			allowEmptyFiles = true
		}
	}
}