package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;

public abstract class BlockRaycastFeature extends AbstractFeature {
    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ServerWorld world = context.getSource().getWorld();
        if (player == null || context.getSource().getWorld() == null) {
            throw new CommandSyntaxException(null, Text.of("This command is client-side only."));
        }
        double maxDistance = player.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE);
        BlockHitResult hitresult = (BlockHitResult)player.raycast(maxDistance, 1.0f, false);

        if (hitresult == null) {
            if (requiresBlock()) {
                throw new SimpleCommandExceptionType(Text.literal("You must be looking at a block to use this command.")).create();
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

	protected abstract int execute(CommandContext<ServerCommandSource> context, BlockInfo blockInfo) throws CommandSyntaxException;
}
