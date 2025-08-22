package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BinaryBlockReadFeature {
	public static final BinaryBlockReadFeature INSTANCE = new BinaryBlockReadFeature();

	protected BinaryBlockReadFeature() {
	}

	public void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(
				literal("/read")
					.executes(getCommandForArgumentCount(0))
					.then(argument("offset", IntegerArgumentType.integer(1))
						.executes(getCommandForArgumentCount(1))
						.then(argument("onBlock", BlockStateArgumentType.blockState(registryAccess))
							.executes(getCommandForArgumentCount(2))
							.then(argument("toBase", IntegerArgumentType.integer(2, 16))
								.executes(getCommandForArgumentCount(3))
								.then(argument("reverseBits", BoolArgumentType.bool())
										.executes(getCommandForArgumentCount(4))))))));
	}

	protected Command<ServerCommandSource> getCommandForArgumentCount(int argNum) {
		return context -> execute(context, argNum);
	}

	protected int execute(CommandContext<ServerCommandSource> context, int argCount) throws CommandSyntaxException {
		boolean reverseBits;
		int offset = argCount >= 1 ? IntegerArgumentType.getInteger(context, "offset") : 2;
		BlockState onBlock = argCount >= 2 ?
			BlockStateArgumentType.getBlockState(context, "onBlock").getBlockState() :
			Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true);
		int toBase = argCount >= 3 ? IntegerArgumentType.getInteger(context, "toBase") : 10;
		reverseBits = argCount >= 4 ? BoolArgumentType.getBool(context, "reverseBits") : false;
		return execute(context, offset, onBlock, toBase, reverseBits);
	}

	protected int execute(CommandContext<ServerCommandSource> context, int offset, BlockState onBlock, int toBase, boolean reverseBits) throws CommandSyntaxException {
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
			throw new SimpleCommandExceptionType(Text.of("The selection must have 2 axis the same.")).create();
		}

		var bits = new StringBuilder();
		for (BlockVector3 point = pos1; boundingBox.contains(point); point = point.add(spacingVector)) {
			var pos = new BlockPos(point.x(), point.y(), point.z());
			var actualState = source.getWorld().getBlockState(pos);
			var matches = actualState.equals(onBlock) && actualState.getEntries().equals(onBlock.getEntries());

			bits.append(matches ? 1 : 0);
		}

		if (reverseBits) {
			bits.reverse();
		}

		var output = Integer.toString(Integer.parseInt(bits.toString(), 2), toBase);
		source.sendMessage(Text.of(output));
		return 0;
	}
}
