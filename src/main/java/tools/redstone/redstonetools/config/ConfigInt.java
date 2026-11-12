package tools.redstone.redstonetools.config;

import tools.redstone.redstonetools.RedstoneTools;

public class ConfigInt extends ConfigOption<Integer> {

	private final int min;
	private final int max;

	public ConfigInt(String key, int defaultValue, int min, int max, String description) {
		super(key, defaultValue, description);
		this.min = min;
		this.max = max;
	}

	@Override
	protected Integer parse(String raw) {
		int parsed;

		try {
			parsed = Integer.parseInt(raw.trim());
		} catch (NumberFormatException ex) {
			return null;
		}

		if (parsed < min || parsed > max) {
			RedstoneTools.LOGGER.warn("Config key '{}' accepts {} to {}, clamping {}", key(), min, max, parsed);
			return Math.clamp(parsed, min, max);
		}

		return parsed;
	}
}
