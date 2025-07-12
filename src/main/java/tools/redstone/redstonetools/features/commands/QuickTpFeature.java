package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.utils.PositionUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class QuickTpFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("quicktp")
                .then(argument("distance", DoubleArgumentType.doubleArg())
                .then(argument("includeFluids", BoolArgumentType.bool())
                .executes(context -> new QuickTpFeature().execute(context))))));
    }
    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var player = context.getSource().getPlayer();

	    assert player != null;
	    var targetPosition = getTargetPosition(player, context);

        player.teleport(targetPosition.x, targetPosition.y, targetPosition.z, false);

        return 0;
    }

    private Vec3d getTargetPosition(PlayerEntity player, CommandContext<ServerCommandSource> context) {
        double distance = DoubleArgumentType.getDouble(context, "distance");
        boolean includeFluids = BoolArgumentType.getBool(context, "includeFluids");
        // 8 chunks default, 16 blocks per chunk
        var renderDistanceBlocks = PlayerEntity.getRenderDistanceMultiplier() * 8 * 16;
        var hit = player.raycast(Math.min(distance, renderDistanceBlocks), 0, includeFluids);

        return clampHitPosition(hit).subtract(0, 1.12, 0);
    }

    private Vec3d clampHitPosition(HitResult hit) {
        if (hit.getType() != HitResult.Type.BLOCK) {
            return hit.getPos().subtract(0, 0.5, 0);
        }

        var blockHit = (BlockHitResult) hit;

        var neighbor = RaycastUtils.getBlockHitNeighbor(blockHit);
        var neighborPos = neighbor.getBlockPos();

        return PositionUtils.getBottomPositionOfBlock(neighborPos);
    }
}
