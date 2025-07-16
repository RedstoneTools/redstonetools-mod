package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.world.TeleportTarget;
import tools.redstone.redstonetools.features.AbstractFeature;
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

public class QuickTpFeature extends AbstractFeature {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("quicktp")
                .then(argument("distance", DoubleArgumentType.doubleArg())
                .then(argument("includeFluids", BoolArgumentType.bool())
                .then(argument("resetVelocity", BoolArgumentType.bool())
                .executes(context -> new QuickTpFeature().execute(context)))))));
    }
    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var player = context.getSource().getEntity();

        double distance = DoubleArgumentType.getDouble(context, "distance");
        boolean includeFluids = BoolArgumentType.getBool(context, "includeFluids");
        boolean resetVelocity = BoolArgumentType.getBool(context, "resetVelocity");

	    assert player != null;

        var hit = player.raycast(distance, 0, includeFluids);

        var targetPosition = clampHitPosition(hit).subtract(0, 0, 0); // 1.12 makes you fall through the block sometimes.

        player.requestTeleport(targetPosition.x, targetPosition.y, targetPosition.z);
        if (resetVelocity) player.setVelocity(Vec3d.ZERO); player.fallDistance = 0; player.velocityModified = true; // guh

        return 0;
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
