package tools.redstone.redstonetools.config;

import tools.redstone.redstonetools.RedstoneTools;

public abstract class ConfigOption<T> {

	private final String key;
	private final String description;
	private final T defaultValue;
	private T value;

	protected ConfigOption(String key, T defaultValue, String description) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.description = description;
		this.value = defaultValue;
	}

	public String key() {
		return key;
	}

	public String description() {
		return description;
	}

	public T get() {
		return value;
	}

	public void reset() {
		this.value = defaultValue;
	}

	public String write() {
		return String.valueOf(value);
	}

	public void read(String raw) {
		T parsed = parse(raw);

		if (parsed == null) {
			RedstoneTools.LOGGER.warn("Invalid value '{}' for config key '{}', falling back to {}", raw, key, defaultValue);
			this.value = defaultValue;
			return;
		}

		this.value = parsed;
	}

	protected abstract T parse(String raw);
}
