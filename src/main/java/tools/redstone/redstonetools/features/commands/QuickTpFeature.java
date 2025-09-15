package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.utils.PositionUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class QuickTpFeature {
	public static final QuickTpFeature INSTANCE = new QuickTpFeature();

	protected QuickTpFeature() {
	}

	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
			dispatcher.register(literal("quicktp")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(this::parseArguments)
				.then(argument("distance", DoubleArgumentType.doubleArg())
						.executes(this::parseArguments)
						.then(argument("throughFluids", BoolArgumentType.bool())
								.executes(this::parseArguments)
								.then(argument("resetVelocity", BoolArgumentType.bool())
										.executes(this::parseArguments)))));
	}

	protected int parseArguments(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayer();

		if (quicktpingForPlayer.contains(player)) throw new SimpleCommandExceptionType(Text.literal("Already doing a quicktp!")).create();
		quicktpingForPlayer.add(player);
		double distance;
		boolean includeFluids;
		boolean resetVelocity;
		try {
			distance = DoubleArgumentType.getDouble(context, "distance");
		} catch (Exception ignored) {
			distance = 50.0;
		}
		try {
			includeFluids = BoolArgumentType.getBool(context, "throughFluids");
		} catch (Exception ignored) {
			includeFluids = false;
		}
		try {
			resetVelocity = BoolArgumentType.getBool(context, "resetVelocity");
		} catch (Exception ignored) {
			resetVelocity = true;
		}
		double finalDistance = distance;
		boolean finalIncludeFluids = !includeFluids;
		boolean finalResetVelocity = resetVelocity;
		Thread thread = new Thread(() -> {
			try {
				execute(context, finalDistance, finalIncludeFluids, finalResetVelocity);
			} catch (CommandSyntaxException e) {
				throw new RuntimeException(e);
			}
		});
		thread.start();
		Thread t2 = new Thread(() -> {
			try {
				thread.join(10000);
			} catch (InterruptedException ignored) {
			}
			if (thread.isAlive()) {
				player.sendMessage(Text.literal("Quicktp still running after 10 seconds. Canceling quicktp!"));
				quicktpingForPlayer.remove(player);
			}
			thread.interrupt();
		});
		t2.start();
		return 1;
	}

	public static List<PlayerEntity> quicktpingForPlayer = new ArrayList<>();

	protected static void execute(CommandContext<ServerCommandSource> context, double distance, boolean includeFluids, boolean resetVelocity) throws CommandSyntaxException {
		var player = context.getSource().getPlayer();
		assert player != null;
		try {
			var hit = player.raycast(distance, 0, includeFluids);

			var targetPosition = clampHitPosition(hit);

			player.requestTeleport(targetPosition.x, targetPosition.y, targetPosition.z);
			if (resetVelocity) player.setVelocity(Vec3d.ZERO);
			player.fallDistance = 0;
			player.velocityModified = true; // guh
		} finally {
			quicktpingForPlayer.remove(player);
		}
	}

	private static Vec3d clampHitPosition(HitResult hit) {
		if (hit.getType() != HitResult.Type.BLOCK) {
			return hit.getPos().subtract(0, 0.5, 0);
		}

		var blockHit = (BlockHitResult) hit;

		var neighbor = RaycastUtils.getBlockHitNeighbor(blockHit);
		var neighborPos = neighbor.getBlockPos();

		return PositionUtils.getBottomPositionOfBlock(neighborPos);
	}
}
