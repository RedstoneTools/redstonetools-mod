package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.BlockStateNbtUtil;


import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT;
import static tools.redstone.redstonetools.utils.ItemUtils.addExtraNBTText;


public class CopyStateFeature extends PickBlockFeature {
    public static void registerCommand() {
        EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("copystate")
                .executes(context -> new CopyStateFeature().execute(context))));
    }
    @Override
    protected ItemStack getItemStack(CommandContext<FabricClientCommandSource> context, BlockInfo blockInfo) {
        MinecraftClient client = MinecraftClient.getInstance();

	    assert blockInfo != null;
	    ItemStack itemStack = blockInfo.block.getDefaultState().getPickStack(client.world, blockInfo.pos, false);

        if (blockInfo.state.hasBlockEntity()) {
            NbtCompound nbt = blockInfo.entity.toInitialChunkDataNbt(
                    Objects.requireNonNull(client.getNetworkHandler()).getRegistryManager()
            );
            BlockItem.setBlockEntityData(
                    itemStack,
                    blockInfo.entity.getType(),
                    nbt
            );
        }

        itemStack = addBlockStateNbt(itemStack, blockInfo.state);
        return itemStack;
    }

    private ItemStack addBlockStateNbt(ItemStack itemStack, BlockState blockState) {
        addExtraNBTText(itemStack, "BlockState");
        return BlockStateNbtUtil.putPlacement(itemStack, blockState);
    }
}
