plugins {
    id("dev.kikugie.stonecutter")
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}
stonecutter.active("1.21.11")

version = "${project.property("mod_version")}+${stonecutter.current?.version}"

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