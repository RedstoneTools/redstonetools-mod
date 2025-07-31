package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.FeatureUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CopyStateFeature extends PickBlockFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("copystate")
                .executes(context -> FeatureUtils.getFeature(CopyStateFeature.class).execute(context))));
    }

    // all of the warnings caused by this function should be fine. hopefully.
    @Override
    protected ItemStack getItemStack(CommandContext<ServerCommandSource> context, BlockInfo blockInfo) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(blockInfo).block);

        List<Text> lore = new ArrayList<>();
        if (blockInfo.state.getProperties().isEmpty())
            return stack.getItem().getDefaultStack();

        BlockStateComponent component = BlockStateComponent.DEFAULT;
        for (Property prop : blockInfo.state.getProperties()) {
            lore.add(Text.of("   " + prop.getName() + ": " + blockInfo.state.get(prop)));
            component = component.with(prop, blockInfo.state.get(prop));
        }

        stack.set(DataComponentTypes.BLOCK_STATE, component);
        stack.set(DataComponentTypes.LORE, new LoreComponent(lore));

        return stack;
    }
}
