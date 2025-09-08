package tools.redstone.redstonetools.utils;

import tools.redstone.redstonetools.features.commands.ClientDataFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {
	public static List<String> unmodifiedCommand = new ArrayList<>();

	public static String insertVariables(String command) {
		boolean didSomething = true;
		while (didSomething) {
			didSomething = false;
			for (String str : ClientDataFeature.INSTANCE.variables.keySet()) {
				var key = "`" + str + "`";
				if (command.contains(key)) {
					command = command.replaceAll(Pattern.quote(key), ClientDataFeature.INSTANCE.variables.get(str));
					didSomething = true;
				}
			}
		}
		return command;
	}

	/**
	 * Expand the partial argument from shortStr using a token from fullStr.
	 * Examples:
	 *   // gm = gamemode
	 *   "`gm` c", "gamemode creative"  -> "`gm` creative"
	 *   "`gm` ",  "gamemode spectator" -> "`gm` spectator"
	 *   // g = give @s
	 *   "`g` white_wo", "give @s white_wool" -> "`g` white_wool"
	 *   // g = game
	 *   "`g`mode c", "gamemode creative"  -> "`g`mode creative"
	 */
	public static String expand(String shortStr, String fullStr) {
		if (shortStr == null || fullStr == null) return shortStr;
		if (fullStr.trim().isEmpty()) return shortStr;

		boolean endsWithSpace = Character.isWhitespace(shortStr.charAt(shortStr.length() - 1));
		String shortTrim = shortStr.trim();
		String[] shortTokens = shortTrim.isEmpty() ? new String[0] : shortTrim.split("\\s+");
		String[] fullTokens = fullStr.trim().split("\\s+");

		if (shortTokens.length == 0) return fullStr;

		if (endsWithSpace) {
			String lastPrefix = shortTokens[shortTokens.length - 1];

			int idx = -1;
			for (int i = 0; i < fullTokens.length; i++) {
				if (fullTokens[i].equals(lastPrefix) || fullTokens[i].startsWith(lastPrefix)) {
					idx = i;
					break;
				}
			}

			if (idx != -1 && idx + 1 < fullTokens.length) {
				return shortTrim + " " + fullTokens[idx + 1];
			} else {
				return shortTrim + " " + fullTokens[fullTokens.length - 1];
			}
		} else {
			if (shortTokens.length < 2) return shortStr;
			String partial = shortTokens[shortTokens.length - 1];
			String prefix = String.join(" ", Arrays.copyOf(shortTokens, shortTokens.length - 1));

			for (String t : fullTokens) {
				if (t.startsWith(partial)) {
					return prefix.isEmpty() ? t : (prefix + " " + t);
				}
			}
			return fullStr;
		}
	}
}
