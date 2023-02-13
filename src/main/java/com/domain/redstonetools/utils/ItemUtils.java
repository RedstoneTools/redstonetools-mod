package com.domain.redstonetools.utils;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ItemUtils {

    private static final HashMap<String, Integer> ITEM_MAP = new HashMap<>();

    private ItemUtils(){
    }

    public static void register() {
        for (int i = 0; i < Registry.ITEM.size(); i++) {
            Item item = Registry.ITEM.get(i);
            ITEM_MAP.put(item.toString(),i);
        }
    }

    public static int getItemIdFromName(String itemName) {
        return ITEM_MAP.getOrDefault(itemName,-1);
    }

}
