package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.mixin.accessors.MinecraftClientAccessor;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.BlockStateNbtUtil;


import static tools.redstone.redstonetools.utils.ItemUtils.addExtraNBTText;


@AutoService(AbstractFeature.class)
@Feature(name = "Copy State", description = "Gives you a copy of the block you're looking at with its BlockState.", command = "copystate")
public class CopyStateFeature extends PickBlockFeature {
    @Override
    protected Either<ItemStack, Feedback> getItemStack(ServerCommandSource source, BlockInfo blockInfo) {
        MinecraftClient client = MinecraftClient.getInstance();

        ItemStack itemStack = blockInfo.block.getDefaultState().getPickStack(client.world, blockInfo.pos, false);

        if (blockInfo.state.hasBlockEntity()) {
            // FIXME
//            ((MinecraftClientAccessor) client).invokeAddBlockEntityNbt(itemStack, blockInfo.entity);
        }

        itemStack = addBlockStateNbt(itemStack, blockInfo.state);
        return Either.left(itemStack);
    }

    private ItemStack addBlockStateNbt(ItemStack itemStack, BlockState blockState) {
        addExtraNBTText(itemStack, "BlockState");
        return BlockStateNbtUtil.putPlacement(itemStack, blockState);
    }


}
