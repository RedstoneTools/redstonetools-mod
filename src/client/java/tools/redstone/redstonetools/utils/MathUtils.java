package tools.redstone.redstonetools.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class MathUtils {
	public static String handleMat(String exp) {
		exp = exp.replaceAll("\\s+", "");
		String[] components = exp.split("(?<=[*+\\-/()^s])|(?=[*+\\-/()^s])");
		String result;
		try {
			result = calculate(components);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return result;
	}

	private static String calculate(String[] components) {
		double result = Double.parseDouble(reduceMathExpression(components));
		if (Math.round(result) == result) {
			return Long.toString((long)result);
		} else {
			return Double.toString(result);
		}
	}

	private static String reduceMathExpression(String[] components) {
		if (components[0].equals("(")) {
			components = removeAt(components, 0);
			for (int i = 0; i < components.length; i++) {
				System.out.println(Arrays.toString(components));
				if (components[i].equals("(")) {
					components = ArrayUtils.addAll(Arrays.copyOfRange(components, 0, i), reduceMathExpression(Arrays.copyOfRange(components, i+1, components.length)));
					i = 0;
				}
				if (components[i].equals(")")) {
					components = removeAt(components, i);
				}
			}
		}
		if (components.length == 1) return components[0];
		boolean done = false;
		int loops = 0;
		while (!done) {
			for (int i = 0; i < components.length; i++) {
				if (components[i].equals(")")) {
					components = removeAt(components, i);
					i = 0;
					continue;
				}
				if (i != components.length -1 && components[i+1].equals("s")) {
					double stacks = 0;
					double left = 0;
					boolean foundLeft = true;
					try { stacks = Double.parseDouble(components[i]);
					} catch (Exception ignored) {}

					try { left = Double.parseDouble(components[i+2]);
					} catch (Exception ignored) {foundLeft = false;}
					components[i] = Double.toString(stacks*64 + left);
					components = removeAt(components, i+1); // removes the s
					if (foundLeft) components = removeAt(components, i+1); // removes left, if it exists
					i = 0;
					continue;
				}
				if (i != 0 && i != components.length - 1) {
					if (components[i+1].equals("(")) {
						components = ArrayUtils.addAll(Arrays.copyOfRange(components, 0, i+1), reduceMathExpression(Arrays.copyOfRange(components, i+1, components.length)));
						i = 0;
						continue;
					}
					boolean hasStacks = Arrays.asList(components).contains("s");
					if (hasStacks && (components[i].equals("*") || components[i].equals("+") || components[i].equals("-") || components[i].equals("/") || components[i].equals("^"))) continue;

					boolean hasExp = Arrays.asList(components).contains("^");
					if (hasExp && (components[i].equals("*") || components[i].equals("+") || components[i].equals("-") || components[i].equals("/"))) continue;

					boolean hasMulOrDiv = Arrays.stream(components).anyMatch(s -> s.equals("*") || s.equals("/"));
					if (hasMulOrDiv && (components[i].equals("+") || components[i].equals("-"))) continue;

					double result = getResult(components, i);

					if (components[i].equals("*") || components[i].equals("+") || components[i].equals("-") || components[i].equals("/") || components[i].equals("^")) {
						components[i - 1] = Double.toString(result);
						components = removeAt(components, i);
						components = removeAt(components, i);
					}
				}
			}
			if (components.length == 1) {
				done = true;
			}
			if (loops > 100000) {
				throw new IllegalArgumentException("Something went wrong!");
			}

			loops++;
		}
		return components[0];
	}

	private static double getResult(String[] components, int i) {
		double x = 0;
		double y = 0;
		double result = 0;
		if (components[i].equals("*") || components[i].equals("+") || components[i].equals("-") || components[i].equals("/") || components[i].equals("^")) {
			x = Double.parseDouble(components[i -1]);
			y = Double.parseDouble(components[i +1]);
		}
		if (components[i].equals("*")) result = x*y;
		if (components[i].equals("+")) result = x+y;
		if (components[i].equals("-")) result = x-y;
		if (components[i].equals("/")) result = x/y;
		if (components[i].equals("^")) result = Math.pow(x, y);
		return result;
	}

	private static String[] removeAt(String[] array, int index) {
		if (index < 0 || index >= array.length) {
			throw new IndexOutOfBoundsException();
		}

		String[] result = new String[array.length - 1];
		for (int i = 0, j = 0; i < array.length; i++) {
			if (i != index) {
				result[j++] = array[i];
			}
		}
		return result;
	}
}
