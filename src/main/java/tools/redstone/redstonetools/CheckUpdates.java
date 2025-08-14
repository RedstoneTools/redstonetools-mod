package tools.redstone.redstonetools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static tools.redstone.redstonetools.RedstoneTools.MOD_VERSION;
import static tools.redstone.redstonetools.RedstoneTools.LOGGER;

public class CheckUpdates {
	public volatile static boolean updateCheckSucceeded = false;

	public volatile static MutableText updateStatus =
			(MutableText) Text.of("Redstone Tools Version: " + MOD_VERSION + " (Bug found, report on Github)");
	public volatile static URI uri;

	public static void checkUpdates() {
		try {
			LOGGER.info("Checking for updates...");

			HttpClient client = HttpClient.newBuilder().build();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api.github.com/repositories/597142955/releases/latest"))
					.GET()
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String responseBody = response.body();

			if (response.statusCode() < 200 || 299 < response.statusCode()) {
				LOGGER.error("Got status code {} while trying to check for updates", response.statusCode());
				return;
			}

			Gson gson = new Gson();
			JsonObject release = gson.fromJson(responseBody, JsonObject.class);
			uri = new URI(release.get("html_url").getAsString());
			String newVersion = release.get("tag_name").getAsString();

			LOGGER.info("Found latest version: {}", newVersion);
			if (newVersion.contains("alpha") || newVersion.contains("beta")) {
				LOGGER.info("Not showing an update popup for alpha/beta release, current: {}, new: {}", MOD_VERSION, newVersion);
				return;
			}

			handleVersionCheck(MOD_VERSION, newVersion);

			updateCheckSucceeded = true;
		} catch (Exception e) {
			LOGGER.warn("Failed to check for RedstoneTools updates");
		}
	}

	private static void handleVersionCheck(String currentVersion, String newVersion) {
		Style underline = Style.EMPTY;

		currentVersion = currentVersion.replaceFirst("^v", "");
		newVersion = newVersion.replaceFirst("^v", "");

		int lastDashCurrent = currentVersion.lastIndexOf("-");
		int lastDashNew = newVersion.lastIndexOf("-");

		if (lastDashCurrent == -1 || lastDashNew == -1) {
			LOGGER.warn("Invalid version format: {}, {}", currentVersion, newVersion);
			return;
		}

		String currentMcRange = currentVersion.substring(0, lastDashCurrent);
		String currentModVersion = currentVersion.substring(lastDashCurrent + 1);

		String newMcRange = newVersion.substring(0, lastDashNew);
		String newModVersion = newVersion.substring(lastDashNew + 1);

		String[] mcParts = currentMcRange.split("-");
		String currentMcMin = normalizeMcVersion(mcParts[0]);
		String currentMcMax = mcParts.length > 1 ? normalizeMcVersion(mcParts[1]) : currentMcMin;

		mcParts = newMcRange.split("-");
		String newMcMin = normalizeMcVersion(mcParts[0]);
		String newMcMax = mcParts.length > 1 ? normalizeMcVersion(mcParts[1]) : newMcMin;

		// Check MC compatibility
		if (!mcVersionInRange(currentMcMin, newMcMin, newMcMax) &&
				!mcVersionInRange(currentMcMax, newMcMin, newMcMax)) {
			LOGGER.info("MC version differs, ignoring update.");
			updateStatus = (MutableText) Text.of("RST newer: " + currentModVersion + " (" + currentMcRange + ")" + " [Pre-/Dev-Build]");
			return;
		}

		int currentModNum = Integer.parseInt(currentModVersion.replaceAll("\\D+", ""));
		int newModNum = Integer.parseInt(newModVersion.replaceAll("\\D+", ""));

		if (currentModNum > newModNum) {
			LOGGER.info("GitHub version is older. Local is newer (dev build?)");
			updateStatus = (MutableText) Text.of("RST newer: " + currentModVersion + " (" + currentMcRange + ")" + " [Pre-/Dev-Build]");
		} else if (currentModNum == newModNum) {
			LOGGER.info("Mod is up to date.");
			updateStatus = (MutableText) Text.of("RST up to date: " + currentModVersion + " (" + currentMcRange + ")");
		} else {
			LOGGER.info("Newer mod version available! Local: {}, New: {}", currentModNum, newModNum);
			updateStatus = (MutableText) Text.of("Redstone Tools " + currentModVersion + " (");
			updateStatus.append(Text.of("Click to Update to " + newModVersion + " [" + newMcRange + "]").getWithStyle(underline.withUnderline(true)).getFirst());
			updateStatus.append(")");
		}
	}

	private static String normalizeMcVersion(String version) {
		if (version.matches("\\d+\\.\\d+$")) {
			version += ".0";
		}
		return version;
	}

	private static boolean mcVersionInRange(String target, String min, String max) {
		return compareVersionStrings(target, min) >= 0 && compareVersionStrings(target, max) <= 0;
	}

	private static int compareVersionStrings(String v1, String v2) {
		String[] parts1 = v1.split("\\.");
		String[] parts2 = v2.split("\\.");
		for (int i = 0; i < Math.max(parts1.length, parts2.length); i++) {
			int p1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
			int p2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
			if (p1 != p2) return Integer.compare(p1, p2);
		}
		return 0;
	}
}
