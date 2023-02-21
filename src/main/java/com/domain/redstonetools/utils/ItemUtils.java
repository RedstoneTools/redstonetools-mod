package com.domain.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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

    public static BlockState getPlacementState(ItemStack stack) {
        Item type = stack.getItem();
        if (!(type instanceof BlockItem blockItem))
            return null;
        return blockItem.getBlock().getDefaultState(); // TODO: get actual placed block using getPlacementState()
    }
}
