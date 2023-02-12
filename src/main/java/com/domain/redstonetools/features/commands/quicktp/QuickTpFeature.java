package com.domain.redstonetools.features.commands.quicktp;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.commands.CommandFeature;
import com.domain.redstonetools.utils.PositionUtils;
import com.domain.redstonetools.utils.RaycastUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

@Feature(name = "quicktp")
public class QuickTpFeature extends CommandFeature<QuickTpOptions> {
    @Override
    protected int execute(ServerCommandSource source, QuickTpOptions options) throws CommandSyntaxException {
        var player = source.getPlayer();

        var targetPosition = getTargetPosition(player, options);

        player.teleport(targetPosition.x, targetPosition.y, targetPosition.z);

        return Command.SINGLE_SUCCESS;
    }

    private Vec3d getTargetPosition(PlayerEntity player, QuickTpOptions options) {
        var hit = player.raycast(10f/*options.distance.getValue()*/, 0, /*options.includeLiquids.getValue()*/false);

        return clampHitPosition(hit);
    }

    private Vec3d clampHitPosition(HitResult hit) {
        if (!(hit instanceof BlockHitResult blockHit)) {
            return hit.getPos();
        }

        return PositionUtils.getBottomPositionOfBlock(RaycastUtils.getBlockHitNeighbor(blockHit).getBlockPos());
    }
}
