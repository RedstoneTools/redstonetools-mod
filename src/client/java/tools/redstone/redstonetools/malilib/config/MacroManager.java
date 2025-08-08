package tools.redstone.redstonetools.malilib.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.malilib.widget.MacroBase;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MacroManager {
	private static Path macrosFilePath;
	private static final List<MacroBase> macros = new ArrayList<>();
	public static List<MacroBase> getAllMacros() {
		return macros;
	}

	public static void addMacro(MacroBase macro) {
		macros.add(macro);
	}
	public static void removeMacro(MacroBase macro) {
		macros.remove(macro);
	}
	public static void clear() {
		macros.clear();
	}

	public static void init() {
		macrosFilePath = MinecraftClient.getInstance().runDirectory.toPath()
				.resolve("config")
				.resolve("redstonetools")
				.resolve("macros.json");
	}

	public static JsonObject toJson() {
		JsonObject obj = new JsonObject();
		JsonArray arr = new JsonArray();
		for (MacroBase macro : macros) {
			arr.add(macro.toJson());
		}

		if (!arr.isEmpty()) {
			obj.add("macros", arr);
		}

		return obj;
	}

	public static void fromJson(JsonObject obj) {
		clear();

		if (JsonUtils.hasArray(obj, "macros")) {
			JsonArray arr = obj.get("macros").getAsJsonArray();

			for (int i = 0; i < arr.size(); ++i) {
				JsonElement el = arr.get(i);

				if (el.isJsonObject()) {
					JsonObject o = el.getAsJsonObject();

					if (JsonUtils.hasString(o, "type")) {
						MacroBase macro = new MacroBase("", "A", new ArrayList<>());
						macro.fromJson(o);
						addMacro(macro);
					}
				}
			}
		}
	}
}
