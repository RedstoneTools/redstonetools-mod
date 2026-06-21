package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.ColoredBlockType;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

public class ColoredFeature extends PickBlockFeature {
	public static final ColoredFeature INSTANCE = new ColoredFeature();

	protected ColoredFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(net.minecraft.commands.Commands.literal("colored")
			.requires(Commands.PERMISSION_LEVEL_2)
			.executes(this::execute)
			.then(net.minecraft.commands.Commands.argument("blockType", StringArgumentType.string()).suggests(ArgumentUtils.COLORED_BLOCK_TYPE_SUGGESTION_PROVIDER)
				.executes(this::execute)));
	}

	public static ColoredBlockType blockType;

	@Override
	protected boolean requiresBlock() {
		return false;
	}

	@Override
	protected ItemStack getItemStack(CommandContext<CommandSourceStack> context, @Nullable BlockInfo blockInfo) {
		var color = blockInfo == null
				? BlockColor.WHITE
				: BlockColor.fromBlock(blockInfo.block);

		var coloredBlock = blockType.withColor(color);

		return new ItemStack(coloredBlock.toBlock());
	}

	@Override
	protected int execute(CommandContext<CommandSourceStack> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
		try {
			blockType = ArgumentUtils.parseColoredBlockType(context, "blockType");
		} catch (Exception e) {
			blockType = ColoredBlockType.WOOL;
		}
		return super.execute(context, blockInfo);
	}
}
