package tools.redstone.redstonetools.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;

public class RaycastUtils {
	private RaycastUtils() {
	}

	public static BlockHitResult getBlockHitNeighbor(BlockHitResult hit) {
		var sideOffset = hit.getSide().getUnitVector();

		var newBlockPos = hit.getBlockPos().add((int) sideOffset.x, (int) sideOffset.y, (int) sideOffset.z);

		return hit.withBlockPos(newBlockPos);
	}

	public static BlockHitResult rayCastFromEye(PlayerEntity player, float reach) {
		return player.world().raycast(new RaycastContext(
				player.getEyePos(),
				player.getEyePos().add(player.getRotationVector().multiply(reach)),
				RaycastContext.ShapeType.COLLIDER,
				RaycastContext.FluidHandling.NONE,
				player
		));
	}
}
