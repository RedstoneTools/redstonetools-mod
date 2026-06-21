package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class BinaryBlockReadFeature {
	public static final BinaryBlockReadFeature INSTANCE = new BinaryBlockReadFeature();

	protected BinaryBlockReadFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(
			literal("/read")
				.requires(Commands.PERMISSION_LEVEL_2)
				.executes(getCommandForArgumentCount(0))
				.then(argument("offset", IntegerArgumentType.integer(1))
					.executes(getCommandForArgumentCount(1))
					.then(argument("onBlock", BlockStateArgument.block(registryAccess))
						.executes(getCommandForArgumentCount(2))
						.then(argument("toBase", IntegerArgumentType.integer(2, 16))
							.executes(getCommandForArgumentCount(3))
							.then(argument("reverseBits", BoolArgumentType.bool())
								.executes(getCommandForArgumentCount(4)))))));
	}

	protected Command<CommandSourceStack> getCommandForArgumentCount(int argNum) {
		return context -> execute(context, argNum);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int argCount) throws CommandSyntaxException {
		boolean reverseBits;
		int offset = argCount >= 1 ? IntegerArgumentType.getInteger(context, "offset") : 2;
		BlockState onBlock = argCount >= 2 ?
			BlockStateArgument.getBlock(context, "onBlock").getState() :
			Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true);
		int toBase = argCount >= 3 ? IntegerArgumentType.getInteger(context, "toBase") : 10;
		reverseBits = argCount >= 4 && BoolArgumentType.getBool(context, "reverseBits");
		return execute(context, offset, onBlock, toBase, reverseBits);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int offset, BlockState onBlock, int toBase, boolean reverseBits) throws CommandSyntaxException {
		var source = context.getSource();
		Region selection = WorldEditUtils.getSelection(source.getPlayer());

		var boundingBox = selection.getBoundingBox();
		var pos1 = boundingBox.getPos1();
		var pos2 = boundingBox.getPos2();
		var direction = pos2.subtract(pos1).normalize();

		// prevent infinite loop
		if (direction.lengthSq() == 0) {
			direction = BlockVector3.at(0, 0, 1);
		}

		var spacingVector = direction.multiply(offset);

		if (direction.x() + direction.y() + direction.z() > 1) {
			throw new SimpleCommandExceptionType(Component.nullToEmpty("The selection must have 2 axis the same.")).create();
		}

		var bits = new StringBuilder();
		for (BlockVector3 point = pos1; boundingBox.contains(point); point = point.add(spacingVector)) {
			var pos = new BlockPos(point.x(), point.y(), point.z());
			var actualState = source.getLevel().getBlockState(pos);
			var matches = actualState.equals(onBlock) && actualState.getValues().equals(onBlock.getValues());

			bits.append(matches ? 1 : 0);
		}

		if (reverseBits) {
			bits.reverse();
		}

		var output = Integer.toString(Integer.parseInt(bits.toString(), 2), toBase);
		source.sendSystemMessage(Component.nullToEmpty(output));
		return 0;
	}
}
