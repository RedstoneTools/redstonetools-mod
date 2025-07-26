package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.datafixers.util.Either;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.BlockInfo;

import static tools.redstone.redstonetools.utils.ItemUtils.addExtraNBTText;

@AutoService(AbstractFeature.class)
@Feature(name = "Copy State", description = "Gives you a copy of the block you're looking at with its BlockState.", command = "copystate")
public class CopyStateFeature extends PickBlockFeature {
    @Override
    protected Either<ItemStack, Feedback> getItemStack(ServerCommandSource source, BlockInfo blockInfo) {
        ItemStack itemStack = blockInfo.block().asItem().getDefaultStack();

        if (blockInfo.state().hasBlockEntity()) {
            itemStack.applyComponentsFrom(blockInfo.entity().createComponentMap());
        }

        var state = BlockStateComponent.DEFAULT;
        for (Property<?> property : blockInfo.state().getProperties()) {
            state = state.with(property, blockInfo.state());
        }
        itemStack.set(DataComponentTypes.BLOCK_STATE, state);

        addExtraNBTText(itemStack, "BlockState");

        return Either.left(itemStack);
    }
}
