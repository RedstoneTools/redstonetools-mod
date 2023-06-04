package tools.redstone.redstonetools.mixin.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.toggleable.SlabKeeperFeature;

@Mixin(ClientPlayerInteractionManager.class)
public class SlabKeeperMixin {

    private final SlabKeeperFeature slabKeeperFeature = RedstoneToolsClient.INJECTOR.getInstance(SlabKeeperFeature.class);
    @Redirect(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public void breakBlock(Block instance, WorldAccess world, BlockPos pos, BlockState state) {
        if (!slabKeeperFeature.isEnabled()) {
            instance.onBroken(world, pos, state);
            return;
        }
        if (!(instance instanceof SlabBlock)) return;
        if (state.get(SlabBlock.TYPE) != SlabType.DOUBLE) return;
        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        if (MinecraftClient.getInstance().player == null) return;
        if (MinecraftClient.getInstance().getServer() == null) return;

        BlockHitResult crosshairTarget = (BlockHitResult)MinecraftClient.getInstance().crosshairTarget;

        SlabType type;
        if (crosshairTarget.getSide() == Direction.UP) type = SlabType.BOTTOM;
        else if (crosshairTarget.getSide() == Direction.DOWN) type = SlabType.TOP;
        else type = ((crosshairTarget.getPos().getY() % 1) + (crosshairTarget.getPos().getY() < 0 ? 1 : 0) > 0.5) ? SlabType.BOTTOM : SlabType.TOP;

        instance.onBroken(world, pos, state);
        world.setBlockState(pos, instance.getDefaultState().with(SlabBlock.TYPE, type), 0);
        try {
            MinecraftClient.getInstance().getServer().getWorld(MinecraftClient.getInstance().player.world.getRegistryKey())
                    .setBlockState(pos, instance.getDefaultState().with(SlabBlock.TYPE, type), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
