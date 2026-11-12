package tools.redstone.redstonetools;
//? paper {
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.features.toggleable.WorldEditHelperFeature;
import tools.redstone.redstonetools.utils.BlockBreakCapture;
import tools.redstone.redstonetools.utils.ColoredBlock;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;

public class RedstoneToolsListener implements Listener {
	@EventHandler
	public void onTick(ServerTickEndEvent event) {
		WorldEditHelperFeature.INSTANCE.checkPlayers(Commands.server);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (AutoDustFeature.INSTANCE.isEnabled(event.getPlayer().getUniqueId())) {
			Block placed = event.getBlockPlaced();
			Block above = placed.getRelative(BlockFace.UP);

			if (above.getType().isAir() && ColoredBlock.fromBlockId(placed.getType().getKey().toString()) != null) {
				above.setBlockData(Bukkit.createBlockData(Material.REDSTONE_WIRE));
			}
		}

		// todo: test
		if (AutoRotateFeature.INSTANCE.isEnabled(event.getPlayer().getUniqueId()))  {
			Block block = event.getBlockPlaced();
			BlockData data = block.getBlockData();

			BlockData rotated = data.clone();
			rotated.rotate(org.bukkit.block.structure.StructureRotation.CLOCKWISE_180);

			if (!rotated.matches(data)) {
				block.setBlockData(rotated, false);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
		Block broken = event.getBlock();
		BlockPos pos = new BlockPos(broken.getX(), broken.getY(), broken.getZ());

		if (BlockBreakCapture.consume(player, pos)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
			return;
		}

		ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
		InteractionHand hand = event.getHand() == EquipmentSlot.OFF_HAND
			? InteractionHand.OFF_HAND
			: InteractionHand.MAIN_HAND;

		Block clicked = event.getClickedBlock();
		BlockPos pos = new BlockPos(clicked.getX(), clicked.getY(), clicked.getZ());

		if (ClickContainerFeature.handleUse(player, player.level(), hand, pos)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	// TODO: implement this on fabric
	public void onPlayerQuit(PlayerQuitEvent event) {
		BlockBreakCapture.release(event.getPlayer().getUniqueId());
	}
}
//? }