package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ArgumentUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SlabFeature {
	public static final SlabFeature INSTANCE = new SlabFeature();

	private static final String DEFAULT_SLAB = "smooth_stone_slab";

	protected SlabFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection registrationEnvironment) {
		dispatcher.register(literal("slab")
			.requires(Commands.getPerm("slab"))
			.executes(this::executeFromHand)
			.then(argument("type", StringArgumentType.string())
				.suggests(ArgumentUtils.SLAB_SUGGESTION_PROVIDER)
				.executes(this::executeFromType)));
	}

	protected int executeFromType(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var name = StringArgumentType.getString(context, "type");
		var slab = createUpsideDownSlab(name);

		if (slab == null) {
			throw new SimpleCommandExceptionType(
				Component.literal("Invalid slab type: " + name)).create();
		}

		player.getInventory().add(slab);
		return 1;
	}

	protected int executeFromHand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var held = player.getMainHandItem();

		if (held.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock) {
			var slab = upsideDown(new ItemStack(blockItem.getBlock(), held.getCount()));
			player.setItemInHand(player.getUsedItemHand(), slab);
			return 1;
		}

		player.getInventory().add(createUpsideDownSlab(DEFAULT_SLAB));
		return 1;
	}

	private ItemStack createUpsideDownSlab(String name) {
		var block = findSlabBlock(name);
		if (block == null) {
			return null;
		}

		var stack = new ItemStack(block);
		return stack.isEmpty() ? null : upsideDown(stack);
	}

	private ItemStack upsideDown(ItemStack stack) {
		stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(SlabBlock.TYPE, SlabType.TOP));
		stack.set(DataComponents.CUSTOM_NAME, Component.literal("Upside Down Slab"));
		stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

		return stack;
	}

	private Block findSlabBlock(String name) {
		for (Block block : BuiltInRegistries.BLOCK) {
			if (block instanceof SlabBlock && ArgumentUtils.blockPath(block).equals(name)) {
				return block;
			}
		}

		return null;
	}
}