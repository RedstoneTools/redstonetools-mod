package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.ClientFeatureUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CopyStateFeature extends PickBlockFeature {
    public static void registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("copystate")
                .executes(context -> ClientFeatureUtils.getFeature(CopyStateFeature.class).execute(context))));
    }

    // all of the warnings caused by this function should be fine. hopefully.
    @Override
    protected ItemStack getItemStack(CommandContext<FabricClientCommandSource> context, BlockInfo blockInfo) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(blockInfo).block);

        List<Text> lore = new ArrayList<>();
        lore.add(Text.of("BlockState: "));

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
