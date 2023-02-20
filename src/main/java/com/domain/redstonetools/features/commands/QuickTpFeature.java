package com.domain.redstonetools.features.commands;

import com.domain.redstonetools.features.Feature;
import com.domain.redstonetools.features.arguments.Argument;
import com.domain.redstonetools.utils.PositionUtils;
import com.domain.redstonetools.utils.RaycastUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;

@Feature(name = "Quick TP", description = "Teleports you in the direction you are looking.", command = "quicktp")
public class QuickTpFeature extends CommandFeature {
    public static final Argument<Float> distance = Argument
            .ofType(floatArg(1.0f))
            .withDefault(25.0f);
    public static final Argument<Boolean> includeFluids = Argument
            .ofType(bool())
            .withDefault(false);

    @Override
    protected int execute(ServerCommandSource source) throws CommandSyntaxException {
        var player = source.getPlayer();

        var targetPosition = getTargetPosition(player);

        player.teleport(targetPosition.x, targetPosition.y, targetPosition.z);

        return Command.SINGLE_SUCCESS;
    }

    private Vec3d getTargetPosition(PlayerEntity player) {
        var hit = player.raycast(distance.getValue(), 0, includeFluids.getValue());

        return clampHitPosition(hit);
    }

    private Vec3d clampHitPosition(HitResult hit) {
        if (!(hit instanceof BlockHitResult blockHit)) {
            return hit.getPos();
        }

        return PositionUtils.getBottomPositionOfBlock(RaycastUtils.getBlockHitNeighbor(blockHit).getBlockPos());
    }
}
