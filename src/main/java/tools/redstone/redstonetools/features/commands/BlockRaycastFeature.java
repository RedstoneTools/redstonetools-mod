package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import javax.annotation.Nullable;

public abstract class BlockRaycastFeature extends AbstractFeature {
    protected int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            throw new CommandSyntaxException(null, Text.of("This command is client-side only."));
        }

        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            if (requiresBlock()) {
                throw new SimpleCommandExceptionType(Text.literal("You must be looking at a block to use this command.")).create();
            } else {
                return execute(context, null);
            }
        }

        var blockPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
        var blockState = client.world.getBlockState(blockPos);
        var blockEntity = client.world.getBlockEntity(blockPos);
        var block = blockState.getBlock();

        return execute(context, new BlockInfo(block, blockPos, blockState, blockEntity));
    }

    protected boolean requiresBlock() {
        return true;
    }

	protected abstract int execute(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
