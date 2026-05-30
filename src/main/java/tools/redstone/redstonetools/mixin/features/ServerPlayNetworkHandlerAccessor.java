package tools.redstone.redstonetools.mixin.features;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerPlayNetworkHandlerAccessor {
	@Invoker
	static void callAddBlockDataToItem(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack) {
		throw new IllegalStateException();
	}
}
