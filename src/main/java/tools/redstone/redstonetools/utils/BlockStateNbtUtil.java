package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.HashMap;
import java.util.Map;

public class BlockStateNbtUtil {
    private BlockStateNbtUtil() {
    }

    public static String blockStateToString(BlockState blockState) {
        HashMap<String, Object> valueMap = new HashMap<>();

        for (Property<?> property : blockState.getEntries().keySet()) {
            valueMap.put(property.getName(), blockState.getEntries().get(property));
        }
        if (valueMap.isEmpty()) return null;

        return valueMap.toString();
    }

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
