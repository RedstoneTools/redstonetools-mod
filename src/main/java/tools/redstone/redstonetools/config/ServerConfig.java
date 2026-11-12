package tools.redstone.redstonetools.config;

import tools.redstone.redstonetools.RedstoneTools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//? if paper {
import org.bukkit.configuration.file.YamlConfiguration;
//? } else {
/*import java.util.Properties;
import java.io.Reader;
*///? }

public class ServerConfig {

	public static final ConfigInt THAT_SIZE_LIMIT = new ConfigInt(
		"that.size-limit", 200, 1, 10_000,
		"Maximum size of the selection //that may produce, on any single axis, in blocks.");

	public static final ConfigInt THAT_MAX_TIME_PER_TICK_MS = new ConfigInt(
		"that.max-time-per-tick-ms", 30, 1, 45,
		"""
			How long //that may spend expanding a selection during a single tick, in milliseconds.
			A server tick is 50ms, so values close to that will visibly lag the server.""");

	public static final ConfigInt THAT_MAX_TICKS = new ConfigInt(
		"that.max-ticks", 5, 1, 600,
		"How many ticks //that may spend on one selection before giving up.");

	public static final List<ConfigOption<?>> OPTIONS = List.of(
		THAT_SIZE_LIMIT,
		THAT_MAX_TIME_PER_TICK_MS,
		THAT_MAX_TICKS);

	public static void load(Path file) {
		Map<String, String> raw = readRaw(file);

		for (ConfigOption<?> option : OPTIONS) {
			String value = raw.get(option.key());

			if (value == null) {
				option.reset();
			} else {
				option.read(value);
			}
		}

		write(file);
	}

	//? if fabric {
	/*private static Map<String, String> readRaw(Path file) {
		Map<String, String> raw = new HashMap<>();
		if (!Files.exists(file)) {
			return raw;
		}

		Properties properties = new Properties();
		try (Reader reader = Files.newBufferedReader(file)) {
			properties.load(reader);
		} catch (IOException ex) {
			RedstoneTools.LOGGER.error("Could not read {}, using defaults", file, ex);
			return raw;
		}

		for (ConfigOption<?> option : OPTIONS) {
			String value = properties.getProperty(option.key());
			if (value != null) {
				raw.put(option.key(), value);
			}
		}

		return raw;
	}

	private static void write(Path file) {
		StringBuilder out = new StringBuilder();
		out.append("# Redstone Tools server configuration.\n");
		out.append("# Regenerated on every start: unknown keys are dropped, new ones appear with their default.\n");

		for (ConfigOption<?> option : OPTIONS) {
			out.append('\n');
			for (String line : option.description().split("\n")) {
				out.append("# ").append(line).append('\n');
			}
			out.append(option.key()).append('=').append(option.write()).append('\n');
		}

		try {
			Files.createDirectories(file.getParent());
			Files.writeString(file, out.toString());
		} catch (IOException ex) {
			RedstoneTools.LOGGER.error("Could not write {}", file, ex);
		}
	}
	*///? } else {
	private static Map<String, String> readRaw(Path file) {
		Map<String, String> raw = new HashMap<>();
		if (!Files.exists(file)) {
			return raw;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file.toFile());

		for (ConfigOption<?> option : OPTIONS) {
			if (config.contains(option.key())) {
				raw.put(option.key(), String.valueOf(config.get(option.key())));
			}
		}

		return raw;
	}

	private static void write(Path file) {
		YamlConfiguration config = new YamlConfiguration();
		config.options().setHeader(List.of(
			"Redstone Tools server configuration.",
			"Regenerated on every start: unknown keys are dropped, new ones appear with their default."));

		for (ConfigOption<?> option : OPTIONS) {
			config.set(option.key(), option.get());
			config.setComments(option.key(), List.of(option.description().split("\n")));
		}

		try {
			Files.createDirectories(file.getParent());
			config.save(file.toFile());
		} catch (IOException ex) {
			RedstoneTools.LOGGER.error("Could not write {}", file, ex);
		}
	}
	//? }
}
