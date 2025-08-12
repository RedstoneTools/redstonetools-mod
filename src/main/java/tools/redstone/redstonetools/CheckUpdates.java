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

	public volatile static MutableText updateStatus = (MutableText) Text.of("Redstone Tools Version: " + MOD_VERSION + "(Bug found, report on Github)");
	public volatile static URI uri;

	public static void checkUpdates() {
		try {
			LOGGER.info("Checking for updates...");

			HttpClient client = HttpClient.newBuilder()
					.build();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://api.github.com/repositories/597142955/releases/latest"))
					.GET()
					.build();

//			simulate update checking taking a while
//			Thread.sleep(10000);

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
				LOGGER.info("Not showing an update popup for alpha or beta release, current version: {}, new version: {}", MOD_VERSION, newVersion);
				return;
			}

			Style underline = Style.EMPTY;
			if (RedstoneTools.MOD_VERSION.equals(newVersion)) {
				LOGGER.info("Already up to date, current version: {}", MOD_VERSION);
				updateStatus = (MutableText) Text.of("Redstone Tools " + MOD_VERSION);
			} else {
				LOGGER.info("Found newer version, current version: {}, new version: {}", RedstoneTools.MOD_VERSION, newVersion);
				updateStatus = (MutableText) Text.of("Redstone Tools " + MOD_VERSION + " (");
				updateStatus.append(Text.of("Click to Update").getWithStyle(underline.withUnderline(true)).getFirst());
				updateStatus.append(")");
			}
			updateCheckSucceeded = true;
		} catch (Exception e) {
			LOGGER.warn("Failed to check for RedstoneTools updates");
//            e.printStackTrace();
		}
	}
}
