package tools.redstone.redstonetools.utils;

import tools.redstone.redstonetools.features.commands.ClientDataFeature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tools.redstone.redstonetools.malilib.config.Configs.ClientData.*;

public class StringUtils {
	public static List<String> unmodifiedCommand = new ArrayList<>();

	public static String insertVariablesAndMath(String command) {
		int counter = 0;
		String prevCommand = "";
		while (!prevCommand.equals(command)) {
			prevCommand = command;
			if (counter++ > 10000) return command;

			Pattern pattern = Pattern.compile(Pattern.quote(MATH_BEGIN_STRING.getStringValue()) + "(.*?)" + Pattern.quote(MATH_END_STRING.getStringValue()));
			Matcher matcher = pattern.matcher(command);

			StringBuilder result = new StringBuilder();
			boolean mathReplaced = false;

			while (matcher.find()) {
				String insideBraces = matcher.group(1);
				try {
					String replacement = MathUtils.handleMat(insideBraces);
					matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
				} catch (IllegalArgumentException ignored) {
				}
				mathReplaced = true;
			}
			matcher.appendTail(result);

			if (mathReplaced) {
				command = result.toString();
			}

			for (String str : ClientDataFeature.INSTANCE.variables.keySet()) {
				var key = VARIABLE_BEGIN_STRING.getStringValue() + str + VARIABLE_END_STRING.getStringValue();
				if (command.contains(key) && !command.contains("\\" + key)) {
					String toReplace = ClientDataFeature.INSTANCE.variables.get(str);
					if (!(toReplace == null || toReplace.isEmpty()))
						command = command.replaceAll(Pattern.quote(key), toReplace);
				}
			}
		}
		// final sweep to turn \'var' into 'var'
		for (String str : ClientDataFeature.INSTANCE.variables.keySet()) {
			var key = VARIABLE_BEGIN_STRING.getStringValue() + str + VARIABLE_END_STRING.getStringValue();
			if (command.contains("\\" + key)) {
				command = command.replaceAll(Pattern.quote("\\" + key), key);
			}
		}
		return command;
	}

		public static String expand (String shortStr, String expandedString){
			Pattern tokenPattern = Pattern.compile("'([^']*)'");
			Pattern quotedValuePattern = Pattern.compile("^'([^']*)'$");
			Matcher tokenMatcher = tokenPattern.matcher(shortStr);

			String result = expandedString;

			while (tokenMatcher.find()) {
				String tokenWithQuotes = tokenMatcher.group(0);
				String key = tokenMatcher.group(1);

				Set<String> seen = new HashSet<>();
				String currentKey = key;
				String resolved;

				while (true) {
					if (!seen.add(currentKey)) {
						resolved = null;
						break;
					}
					String val = ClientDataFeature.INSTANCE.variables.get(currentKey);
					if (val == null) {
						resolved = null;
						break;
					}
					Matcher qm = quotedValuePattern.matcher(val);
					if (qm.matches()) {
						currentKey = qm.group(1);
					} else {
						resolved = val;
						break;
					}
				}

				if (resolved == null || resolved.isEmpty()) {
					continue;
				}
				result = result.replaceFirst(Pattern.quote(resolved), Matcher.quoteReplacement(tokenWithQuotes));
			}

			return result;
		}
	}
