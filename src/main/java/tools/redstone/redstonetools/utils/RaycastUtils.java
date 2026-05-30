package tools.redstone.redstonetools.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;

public class RaycastUtils {
	private RaycastUtils() {
	}

	public static BlockHitResult getBlockHitNeighbor(BlockHitResult hit) {
		var sideOffset = hit.getDirection().step();

		var newBlockPos = hit.getBlockPos().offset((int) sideOffset.x, (int) sideOffset.y, (int) sideOffset.z);

		return hit.withPosition(newBlockPos);
	}

	public static BlockHitResult rayCastFromEye(Player player, float reach) {
		return PlayerUtils.getWorld(player).clip(new ClipContext(
				player.getEyePosition(),
				player.getEyePosition().add(player.getLookAngle().scale(reach)),
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				player
		));
	}
}
