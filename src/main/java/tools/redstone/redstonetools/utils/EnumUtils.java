package tools.redstone.redstonetools.utils;

import java.util.Arrays;
import java.util.Locale;

public class EnumUtils {
	private EnumUtils() {
	}

	public static <T extends Enum<T>> T byNameOrNull(T[] values, String name) {
		return Arrays.stream(values)
			.filter(value -> value.name().equalsIgnoreCase(name))
			.findFirst()
			.orElse(null);
	}

	public static <T extends Enum<T>> String[] lowercaseNames(T[] values) {
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			names[i] = values[i].name().toLowerCase(Locale.ROOT);
		}
		return names;
	}
}
