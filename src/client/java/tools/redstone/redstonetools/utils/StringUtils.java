package tools.redstone.redstonetools.utils;

import tools.redstone.redstonetools.features.commands.ClientDataFeature;

import java.util.regex.Pattern;

public class StringUtils {
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
}
