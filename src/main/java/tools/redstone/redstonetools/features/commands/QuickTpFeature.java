package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.FeatureUtils;
import tools.redstone.redstonetools.utils.PositionUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class QuickTpFeature extends AbstractFeature {
    public static void registerCommand() {
        var qtp = FeatureUtils.getFeature(QuickTpFeature.class);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("quicktp")
                .executes(qtp::parseArguments)
                .then(argument("distance", DoubleArgumentType.doubleArg())
                .executes(qtp::parseArguments)
                .then(argument("throughFluids", BoolArgumentType.bool())
                .executes(qtp::parseArguments)
                .then(argument("resetVelocity", BoolArgumentType.bool())
                .executes(qtp::parseArguments))))));
    }

    protected int parseArguments(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        double distance;
        boolean includeFluids;
        boolean resetVelocity;
        try { distance = DoubleArgumentType.getDouble(context, "distance"); }
        catch (Exception ignored) {
            distance = 50.0;
        }
        try { includeFluids = BoolArgumentType.getBool(context, "throughFluids"); }
        catch (Exception ignored) {
            includeFluids = false;
        }
        try { resetVelocity = BoolArgumentType.getBool(context, "resetVelocity"); }
        catch (Exception ignored) {
            resetVelocity = true;
        }
        return execute(context, distance, includeFluids, resetVelocity);
    }

    protected int execute(CommandContext<ServerCommandSource> context, double distance, boolean includeFluids, boolean resetVelocity) throws CommandSyntaxException {
        var player = context.getSource().getPlayer();

	    assert player != null;

        var hit = player.raycast(distance, 0, includeFluids);

        var targetPosition = clampHitPosition(hit);

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
