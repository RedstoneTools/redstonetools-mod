package tools.redstone.redstonetools.mixin.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.SlabKeeperFeature;

@Mixin(ClientPlayerInteractionManager.class)
public class SlabKeeperMixin {

    private final SlabKeeperFeature slabKeeperFeature = RedstoneToolsClient.INJECTOR.getInstance(SlabKeeperFeature.class);
    @Inject(method = "breakBlock", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, World world, BlockState blockState, Block block, FluidState fluidState, boolean bl) {
        if (!slabKeeperFeature.isEnabled()) return;

        if (!(block instanceof SlabBlock)) return;
        if (blockState.get(SlabBlock.TYPE) != SlabType.DOUBLE) return;
        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        if (MinecraftClient.getInstance().player == null) return;
        if (MinecraftClient.getInstance().getServer() == null) return;
        if (world == null) return;

        BlockHitResult crosshairTarget = (BlockHitResult)MinecraftClient.getInstance().crosshairTarget;

        SlabType type;
        if (crosshairTarget.getSide() == Direction.UP) type = SlabType.BOTTOM;
        else if (crosshairTarget.getSide() == Direction.DOWN) type = SlabType.TOP;
        else type = ((crosshairTarget.getPos().getY() % 1) + (crosshairTarget.getPos().getY() < 0 ? 1 : 0) > 0.5) ? SlabType.BOTTOM : SlabType.TOP;

        block.onBroken(world, pos, blockState);
        world.setBlockState(pos, block.getDefaultState().with(SlabBlock.TYPE, type), 0);
        ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getWorld(MinecraftClient.getInstance().player.world.getRegistryKey());
        if (serverWorld == null) return;
        serverWorld.setBlockState(pos, block.getDefaultState().with(SlabBlock.TYPE, type), 0);

        cir.setReturnValue(true);
        cir.cancel();
    }
}
