package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.utils.PositionUtils;
import tools.redstone.redstonetools.utils.RaycastUtils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class QuickTpFeature {
	public static final QuickTpFeature INSTANCE = new QuickTpFeature();

	protected QuickTpFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(literal("quicktp")
				.requires(Commands.PERMISSION_LEVEL_2)
				.executes(this::parseArguments)
				.then(argument("distance", DoubleArgumentType.doubleArg())
						.executes(this::parseArguments)
						.then(argument("throughFluids", BoolArgumentType.bool())
								.executes(this::parseArguments)
								.then(argument("resetVelocity", BoolArgumentType.bool())
										.executes(this::parseArguments)))));
	}

	protected int parseArguments(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		var player = context.getSource().getPlayer();

		if (quicktpingForPlayer.contains(player)) throw new SimpleCommandExceptionType(Component.literal("Already doing a quicktp!")).create();
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
				player.sendSystemMessage(Component.literal("Quicktp still running after 10 seconds. Canceling quicktp!"));
				quicktpingForPlayer.remove(player);
			}
			thread.interrupt();
		});
		t2.start();
		return 1;
	}

	public static List<Player> quicktpingForPlayer = new ArrayList<>();

	protected static void execute(CommandContext<CommandSourceStack> context, double distance, boolean includeFluids, boolean resetVelocity) throws CommandSyntaxException {
		var player = context.getSource().getPlayer();
		assert player != null;
		try {
			var hit = player.pick(distance, 0, includeFluids);

			var targetPosition = clampHitPosition(hit);

			player.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
			if (resetVelocity) player.setDeltaMovement(Vec3.ZERO);
			player.fallDistance = 0;
			player.connection.send(new ClientboundSetEntityMotionPacket(player));
		} finally {
			quicktpingForPlayer.remove(player);
		}
	}

	private static Vec3 clampHitPosition(HitResult hit) {
		if (hit.getType() != HitResult.Type.BLOCK) {
			return hit.getLocation().subtract(0, 0.5, 0);
		}

		var blockHit = (BlockHitResult) hit;

		var neighbor = RaycastUtils.getBlockHitNeighbor(blockHit);
		var neighborPos = neighbor.getBlockPos();

		return PositionUtils.getBottomPositionOfBlock(neighborPos);
	}
}
