package tools.redstone.redstonetools.utils;

//? paper
import com.sk89q.worldedit.bukkit.BukkitAdapter;
//? fabric
//import com.sk89q.worldedit.fabric.FabricAdapter;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.world.World;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface PlatformAdapter {
	//? paper {
	PlatformAdapter INSTANCE = new PaperAdapter();
	//? } else {
	/*PlatformAdapter INSTANCE = new FabricAdapterImpl();
	*///? }

	Player adapt(ServerPlayer player);
	World adapt(Level level);

	//? paper {
	class PaperAdapter implements PlatformAdapter {
		@Override
		public Player adapt(ServerPlayer player) {
			return BukkitAdapter.adapt(player.getBukkitEntity());
		}

		@Override
		public World adapt(Level level) {
			return BukkitAdapter.adapt(level.getWorld());
		}
	}
	//? } else {
	/*class FabricAdapterImpl implements PlatformAdapter {
		@Override
		public Player adapt(ServerPlayer player) {
			//? if <26.1 {
			/^return FabricAdapter.adaptPlayer(player);
			^///? } else {
			return FabricAdapter.get().fromNativePlayer(player);
			 //? }
		}

		@Override
		public World adapt(Level level) {
			//? if <26.1 {
			/^return FabricAdapter.adapt(level);
			^///? } else
			return FabricAdapter.get().fromNativeWorld(level);
		}
	}
	*///? }
}
