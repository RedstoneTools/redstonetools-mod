package com.domain.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for working with block states.
 */
public class BlockStateNbtUtil {
    private BlockStateNbtUtil() {
    }

    /**
     * Stringifies the given block state.
     */
    public static String blockStateToString(BlockState blockState) {
        HashMap<String, Object> valueMap = new HashMap<>();

        for (Property<?> property : blockState.getEntries().keySet()) {
            valueMap.put(property.getName(), blockState.getEntries().get(property));
        }
        if (valueMap.isEmpty()) return null;

        return valueMap.toString();
    }

    /**
     * Parses a string into a block state
     * and returns it.
     *
     * @param defaultState The base state to build on.
     */
    public static BlockState stringToBlockState(String string, BlockState defaultState) {
        BlockState finalState = defaultState;
        Map<String, String> map = stringToMap(string);

        for (String stringProperty : map.keySet()) {
            for (Property property : defaultState.getProperties()) {
                if (property.getName().equals(stringProperty)) {
                    Comparable comparable = propertyValueFromStringValue(property,map.get(stringProperty));
                    if (comparable == null) continue;
                    finalState = finalState.with(property,comparable);
                }
            }

        }

        return finalState;
    }

    // parses a value from string
    // for the given property
    private static Comparable propertyValueFromStringValue(Property property, String string) {
        for (Object o : property.getValues()) {
            if (o.toString().equals(string)) return (Comparable) o;
        }

        return null;
    }

    // TODO: Refactor, this method isn't related to block states
    private static Map<String, String> stringToMap(String string) {
        //https://stackoverflow.com/questions/26485964/how-to-convert-string-into-hashmap-in-java
        string = string.substring(1, string.length() - 1);
        String[] keyValuePairs = string.split(",");
        Map<String, String> map = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }

        return map;
    }
}
