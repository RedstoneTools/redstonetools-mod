package tools.redstone.redstonetools.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.RaycastContext;

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

    public static BlockState getPlacementState(ClientPlayerEntity player, ItemStack stack, float reach) {
        Item type = stack.getItem();
        if (!(type instanceof BlockItem blockItem))
            return null;
        return blockItem.getBlock().getPlacementState(new ItemPlacementContext(
                player,
                player.getActiveHand(),
                stack,
                RaycastUtils.rayCastFromEye(player, reach)
        ));
    }

    public static BlockState getUseState(ClientPlayerEntity player, ItemStack stack, float reach) {
        if (stack == null || stack.getItem() == Items.AIR)
            return null;
        BlockState state = getPlacementState(player, stack, reach);
        if (state == null)
            state = Blocks.BEDROCK.getDefaultState();
        return state;
    }
}
