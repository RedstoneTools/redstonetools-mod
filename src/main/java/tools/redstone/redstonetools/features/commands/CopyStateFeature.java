package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.mixin.AbstractBlockMixin;
import tools.redstone.redstonetools.mixin.features.ServerPlayNetworkHandlerAccessor;
import tools.redstone.redstonetools.utils.BlockInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopyStateFeature extends PickBlockFeature {
	public static final CopyStateFeature INSTANCE = new CopyStateFeature();

	protected CopyStateFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(net.minecraft.commands.Commands.literal("copystate").requires(Commands.PERMISSION_LEVEL_2).executes(this::execute));
	}

	@Override
	protected ItemStack getItemStack(CommandContext<CommandSourceStack> context, BlockInfo blockInfo) {
		Objects.requireNonNull(blockInfo);
		ItemStack stack = ((AbstractBlockMixin) blockInfo.state.getBlock()).callGetCloneItemStack(context.getSource().getLevel(), blockInfo.pos, blockInfo.state, true);

		ServerPlayNetworkHandlerAccessor.callAddBlockDataToItem(blockInfo.state, context.getSource().getLevel(), blockInfo.pos, stack);

		List<Component> lore = new ArrayList<>();
		if (blockInfo.entity != null) {
			lore.add(Component.literal("Has block entity data"));
		}

		if (!blockInfo.state.getProperties().isEmpty()) {
			BlockItemStateProperties component = BlockItemStateProperties.EMPTY;
			for (Property<?> property : blockInfo.state.getProperties()) {
				component = addToLoreAndComponent(lore, component, blockInfo.state, property);
			}

			stack.set(DataComponents.BLOCK_STATE, component);
		}
		stack.set(DataComponents.LORE, new ItemLore(lore));

		return stack;
	}

	private static <T extends Comparable<T>> BlockItemStateProperties addToLoreAndComponent(List<Component> lore, BlockItemStateProperties component, BlockState state, Property<T> property) {
		lore.add(Component.nullToEmpty("   " + property.getName() + ": " + state.getValue(property)));
		return component.with(property, state.getValue(property));
	}
}
