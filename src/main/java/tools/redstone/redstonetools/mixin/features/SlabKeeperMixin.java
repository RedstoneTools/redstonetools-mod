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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.SlabKeeperFeature;

@Mixin(ClientPlayerInteractionManager.class)
public class SlabKeeperMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private final SlabKeeperFeature slabKeeperFeature = RedstoneToolsClient.INJECTOR.getInstance(SlabKeeperFeature.class);
    @Inject(method = "breakBlock", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, World world, BlockState blockState, Block block, FluidState fluidState, boolean bl) {

        if (!slabKeeperFeature.isEnabled()) return;

        if (!(block instanceof SlabBlock)) return;
        if (blockState.get(SlabBlock.TYPE) != SlabType.DOUBLE) return;

        if (this.client.crosshairTarget == null) return;
        if (this.client.player == null) return;
        if (this.client.getServer() == null) return;

        SlabType type = getSlabType((BlockHitResult) this.client.crosshairTarget);

        world.setBlockState(pos, block.getDefaultState().with(SlabBlock.TYPE, type), 0);
        ServerWorld serverWorld = this.client.getServer().getWorld(this.client.player.world.getRegistryKey());
        if (serverWorld == null) return;
        serverWorld.setBlockState(pos, block.getDefaultState().with(SlabBlock.TYPE, type), 0);
    }

    @Unique
    private static SlabType getSlabType(BlockHitResult crosshairTarget) {
        switch (crosshairTarget.getSide()) {
            case UP: return SlabType.BOTTOM;
            case DOWN: return SlabType.TOP;
            default: {
                double crosshairTargetOnBlockY = crosshairTarget.getPos().getY() % 1;
                if (crosshairTargetOnBlockY < 0) crosshairTargetOnBlockY += 1;
               return (crosshairTargetOnBlockY > 0.5) ? SlabType.BOTTOM : SlabType.TOP;
            }
        }
    }
}
