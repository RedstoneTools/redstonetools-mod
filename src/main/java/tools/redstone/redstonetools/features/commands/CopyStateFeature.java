package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.mixin.AbstractBlockMixin;
import tools.redstone.redstonetools.mixin.features.ServerPlayNetworkHandlerAccessor;
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

	@Override
	protected ItemStack getItemStack(CommandContext<ServerCommandSource> context, BlockInfo blockInfo) {
		Objects.requireNonNull(blockInfo);
		ItemStack stack = ((AbstractBlockMixin) blockInfo.state.getBlock()).callGetPickStack(context.getSource().getWorld(), blockInfo.pos, blockInfo.state, true);

		ServerPlayNetworkHandlerAccessor.callCopyBlockDataToStack(blockInfo.state, context.getSource().getWorld(), blockInfo.pos, stack);

		List<Text> lore = new ArrayList<>();
		if (blockInfo.entity != null) {
			lore.add(Text.literal("Has block entity data"));
		}

		if (!blockInfo.state.getProperties().isEmpty()) {
			BlockStateComponent component = BlockStateComponent.DEFAULT;
			for (Property<?> property : blockInfo.state.getProperties()) {
				component = addToLoreAndComponent(lore, component, blockInfo.state, property);
			}

			stack.set(DataComponentTypes.BLOCK_STATE, component);
		}
		stack.set(DataComponentTypes.LORE, new LoreComponent(lore));

		return stack;
	}

	private static <T extends Comparable<T>> BlockStateComponent addToLoreAndComponent(List<Text> lore, BlockStateComponent component, BlockState state, Property<T> property) {
		lore.add(Text.of("   " + property.getName() + ": " + state.get(property)));
		return component.with(property, state.get(property));
	}
}
