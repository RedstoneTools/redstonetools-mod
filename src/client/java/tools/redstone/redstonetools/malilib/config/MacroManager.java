package tools.redstone.redstonetools.malilib.config;

import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MacroManager {
	private static final Path MACROS_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath()
			.resolve("config")
			.resolve("redstonetools")
			.resolve("macros.json");
	private static List<MacroBase> macros;

	public static List<MacroBase> getAllMacros() {
		return macros;
	}

	public static void loadMacros() {
		javax.json.JsonArray macrosJson = null;
		try {
			Files.createDirectories(MACROS_FILE_PATH.getParent());
			if (Files.exists(MACROS_FILE_PATH)) {
				var reader = Json.createReader(new FileReader(MACROS_FILE_PATH.toFile()));
				macrosJson = reader.readArray();
				reader.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (macrosJson == null) {
			macros = new ArrayList<>();
			macros.addAll(getDefaultMacros());
		} else {
			macros = getMacrosFromJson(macrosJson);
		}
	}

	public static List<MacroBase> getMacros() {
		return macros;
	}

	public static MacroBase getMacro(String name) {
		for (MacroBase macro : macros) {
			if (macro.getName().equals(name)) {
				return macro;
			}
		}

		return null;
	}

	public static boolean nameExists(String name, MacroBase exclude) {
		for (MacroBase macro : macros) {
			if (macro == exclude) continue;
			if (macro.getName().equals(name)) return true;
		}
		return false;
	}

	public static void saveChanges() {
		try {
			Files.createDirectories(MACROS_FILE_PATH.getParent());
		} catch (IOException e) {
			e.printStackTrace();
		}

		var macrosJson = Json.createArrayBuilder();
		for (MacroBase macro : macros) {
			macrosJson.add(getMacroJson(macro));
		}

		try (var writer = Json.createWriter(new FileWriter(MACROS_FILE_PATH.toFile()))) {
			writer.writeArray(macrosJson.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static javax.json.JsonObject getMacroJson(MacroBase macro) {
		var actionsJson = Json.createArrayBuilder();
		for (CommandAction action : macro.actions) {
			actionsJson.add(getActionJson(action));
		}

		return Json.createObjectBuilder()
				.add("name", macro.getName())
				.add("enabled", macro.isEnabled())
				.add("key", macro.hotkey.getKeybind().getStringValue())
				.add("actions", actionsJson)
				.build();
	}

	private static javax.json.JsonObject getActionJson(CommandAction action) {
		if (action instanceof CommandAction commandAction) {
			return Json.createObjectBuilder()
					.add("type", "command")
					.add("command", commandAction.command)
					.build();
		}

		throw new RuntimeException("Unknown action type: " + action.getClass().getName());
	}

	public static List<MacroBase> getDefaultMacros() {
		return List.of(
				createCommandMacro("redstoner", new String[]{
						"/gamerule doTileDrops false",
						"/gamerule doTraderSpawning false",
						"/gamerule doWeatherCycle false",
						"/gamerule doDaylightCycle false",
						"/gamerule doMobSpawning false",
						"/gamerule doContainerDrops false",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/time set noon",
						"/weather clear"
				}),
				createCommandMacro("copystate", new String[]{"/copystate"}),
				createCommandMacro("itembind", new String[]{"/itembind"}),
				createCommandMacro("minsel", new String[]{"//minsel"}),
				createCommandMacro("quicktp", new String[]{"/quicktp"}),
				createCommandMacro("binaryblockread", new String[]{"//binaryblockread"}),
				createCommandMacro("rstack", new String[]{"//rstack"}),
				createCommandMacro("update", new String[]{"//update"})
		);
	}

	private static MacroBase createCommandMacro(String name, String[] commands) {
		var actions = new CommandAction[commands.length];
		for (int i = 0; i < commands.length; i++) {
			actions[i] = new CommandAction(commands[i]);
		}

		return new MacroBase(name, "", List.of(actions));
	}

	private static List<MacroBase> getMacrosFromJson(javax.json.JsonArray macrosJson) {
		List<MacroBase> macros = new ArrayList<>();

		for (int i = 0; i < macrosJson.size(); i++) {
			macros.add(getMacroFromJson(macrosJson.getJsonObject(i)));
		}

		return macros;
	}

	private static MacroBase getMacroFromJson(javax.json.JsonObject macroJson) {
		String name = macroJson.getString("name");
		boolean enabled = macroJson.getBoolean("enabled");
		String key = macroJson.getString("key");
		var actions = getActionsFromJson(macroJson.getJsonArray("actions"));

		return new MacroBase(name, key, actions, enabled);
	}

	private static List<CommandAction> getActionsFromJson(JsonArray actionsJson) {
		List<CommandAction> actions = new ArrayList<>();

		for (int i = 0; i < actionsJson.size(); i++) {
			actions.add(getActionFromJson(actionsJson.getJsonObject(i)));
		}

		return actions;
	}

	private static CommandAction getActionFromJson(JsonObject actionJson) {
		var type = actionJson.getString("type");

		if ("command".equals(type)) {
			return new CommandAction(actionJson.getString("command"));
		}

		throw new RuntimeException("Unknown action type: " + type);
	}

	public static void removeMacro(MacroBase macro) {
		macros.remove(macro);
	}

	public static void addMacro(MacroBase macro) {
		macros.add(macro);
	}
}
