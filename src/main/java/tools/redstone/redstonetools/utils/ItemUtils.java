package tools.redstone.redstonetools.utils;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ItemUtils {
    private ItemUtils(){
    }

    private static final HashMap<String, Item> ITEM_MAP = new HashMap<>();

    static {
        for (Item item : Registry.ITEM) {
            ITEM_MAP.put(item.toString(), item);
        }
    }

    public static Item getItemByName(String itemName) {
        return ITEM_MAP.getOrDefault(itemName,null);
    }
}
