package tools.redstone.redstonetools.malilib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MacroManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static boolean shouldMute;
	private static final Path MACROS_FILE_PATH = MinecraftClient.getInstance().runDirectory.toPath()
		.resolve("config")
		.resolve("redstonetools")
		.resolve("macros.json");
	private static List<MacroBase> macros = new ArrayList<>();

	public static List<MacroBase> getAllMacros() {
		return macros;
	}


	private static final Type MACRO_LIST_TYPE = new TypeToken<List<MacroStructure>>() {
	}.getType();

	public static void saveChanges() {
		List<MacroStructure> macroStructure = new ArrayList<>();
		for (MacroBase macro : macros) {
			macroStructure.add(new MacroStructure(
				macro.getName(),
				macro.hotkey.getKeybind().getStringValue(),
				macro.isEnabled(),
				macro.muted,
				macro.actions
			));
		}
		try {
			if (MACROS_FILE_PATH.getParent() != null) Files.createDirectories(MACROS_FILE_PATH.getParent());

			Path tmp = MACROS_FILE_PATH.resolveSibling(MACROS_FILE_PATH.getFileName().toString() + ".tmp");
			try (BufferedWriter writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
				GSON.toJson(macroStructure, MACRO_LIST_TYPE, writer);
			}
			Files.move(tmp, MACROS_FILE_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (IOException ignored) {
		}
	}

	public static void loadMacros() {
		if (!Files.exists(MACROS_FILE_PATH)) {
			macros = getDefaultMacros();
			return;
		}
		try (BufferedReader reader = Files.newBufferedReader(MACROS_FILE_PATH, StandardCharsets.UTF_8)) {
			List<MacroStructure> list = GSON.fromJson(reader, MACRO_LIST_TYPE);
			if (list == null) {
				macros = getDefaultMacros();
				return;
			}
			for (MacroStructure macro : list) {
				macros.add(new MacroBase(
					macro.name,
					macro.key,
					macro.actions,
					macro.enabled,
					macro.muted
				));
			}
		} catch (
			IOException ignored) {
			macros = getDefaultMacros();
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
				"/weather clear"
			}),
			createCommandMacro("copystate", new String[]{"/copystate" }),
			createCommandMacro("itembind", new String[]{"/itembind" }),
			createCommandMacro("minsel", new String[]{"//minsel" }),
			createCommandMacro("quicktp", new String[]{"/quicktp" }),
			createCommandMacro("binaryblockread", new String[]{"//binaryblockread" }),
			createCommandMacro("rstack", new String[]{"//rstack" }),
			createCommandMacro("update", new String[]{"//update" })
		);
	}

	public static MacroBase createCommandMacro(String name, String[] commands) {
		var actions = new CommandAction[commands.length];
		for (int i = 0; i < commands.length; i++) {
			actions[i] = new CommandAction(commands[i]);
		}

		return new MacroBase(name, "", List.of(actions));
	}

	public static void removeMacro(MacroBase macro) {
		macros.remove(macro);
	}

	public static void addMacroToTop(MacroBase macroBase) {
		if (MacroManager.nameExists(macroBase.getName(), null)) {
			macroBase.setName(macroBase.getName() + " " + UUID.randomUUID());
		}
		macros.addFirst(macroBase);
	}

	record MacroStructure(
		String name,
		String key,
		boolean enabled,
		boolean muted,
		List<CommandAction> actions
	) {

	}
}
