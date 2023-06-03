package tools.redstone.redstonetools.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ItemUtils {
    private ItemUtils(){
    }

    //TODO the 'getItemByName' part can propably be deleted
    private static final HashMap<String, Item> ITEM_MAP = new HashMap<>();

    static {
        for (Item item : Registry.ITEM) {
            ITEM_MAP.put(item.toString(), item);
        }
    }

    public static Item getItemByName(String itemName) {
        return ITEM_MAP.getOrDefault(itemName,null);
    }

    public static void addExtraNBTText(ItemStack itemStack, String text) {
        NbtCompound displayNbt = itemStack.getSubNbt("display");
        NbtList loreList = new NbtList();

        if (displayNbt == null) {
            displayNbt = new NbtCompound();
        } else {
            loreList = (NbtList) displayNbt.get("Lore");
        }

        loreList.add(NbtString.of("\"(+"+text +")\""));
        displayNbt.put("Lore", loreList);
        itemStack.setSubNbt("display", displayNbt);
    }

}
