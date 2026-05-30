package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import tools.redstone.redstonetools.utils.BlockInfo;

public abstract class BlockRaycastFeature {
	protected int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayer();
		ServerLevel world = context.getSource().getLevel();
		if (player == null || context.getSource().getLevel() == null) {
			throw new CommandSyntaxException(null, Component.nullToEmpty("This command is client-side only."));
		}
		double maxDistance = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
		BlockHitResult hitresult = (BlockHitResult) player.pick(maxDistance, 1.0f, false);

		if (hitresult.getType() == HitResult.Type.MISS) {
			if (requiresBlock()) {
				throw new SimpleCommandExceptionType(Component.literal("You must be looking at a block to use this command.")).create();
			} else {
				return execute(context, null);
			}
		}

		var blockPos = (hitresult).getBlockPos();
		var blockState = world.getBlockState(blockPos);
		var blockEntity = world.getBlockEntity(blockPos);
		var block = blockState.getBlock();

		return execute(context, new BlockInfo(block, blockPos, blockState, blockEntity));
	}

	protected boolean requiresBlock() {
		return true;
	}

	protected abstract int execute(CommandContext<CommandSourceStack> context, BlockInfo blockInfo) throws CommandSyntaxException;
}
