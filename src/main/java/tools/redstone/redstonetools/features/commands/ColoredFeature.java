package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.BlockColor;
import tools.redstone.redstonetools.utils.BlockInfo;
import tools.redstone.redstonetools.utils.ColoredBlockType;
import javax.annotation.Nullable;

public class ColoredFeature extends PickBlockFeature {
	public static final ColoredFeature INSTANCE = new ColoredFeature();

	protected ColoredFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(CommandManager.literal("colored")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(this::execute)
				.then(CommandManager.argument("blockType", StringArgumentType.string()).suggests(ArgumentUtils.COLORED_BLOCK_TYPE_SUGGESTION_PROVIDER)
					.executes(this::execute)));
	}

	public static ColoredBlockType blockType;

	protected boolean requiresBlock() {
		return false;
	}

	protected ItemStack getItemStack(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) {
		var color = blockInfo == null
				? BlockColor.WHITE
				: BlockColor.fromBlock(blockInfo.block);

		var coloredBlock = blockType.withColor(color);

		return new ItemStack(coloredBlock.toBlock());
	}

	@Override
	protected int execute(CommandContext<ServerCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
		try {
			blockType = ArgumentUtils.parseColoredBlockType(context, "blockType");
		} catch (Exception e) {
			blockType = ColoredBlockType.WOOL;
		}
		return super.execute(context, blockInfo);
	}
}
