package tools.redstone.redstonetools.mixin.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.*;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.commands.BigDustHeightFeature;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

import java.util.Map;

@Pseudo
@Mixin(RedstoneWireBlock.class)
public class RedstoneHitboxMixin {

    @Final
    @Shadow
    private static Map<BlockState, VoxelShape> SHAPES;

    @Final
    @Shadow
    public static IntProperty POWER;

    /**
     * @author JayMensink
     * @reason Issue #279: Option to make redstone hitbox bigger
     */
    @Overwrite
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BigDustFeature bigDustFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustFeature.class);
        BigDustHeightFeature bigDustHeightFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustHeightFeature.class);

        if (bigDustFeature.isEnabled()) {
            return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, bigDustHeightFeature.customHeight, 16.0);
        }

        return SHAPES.get(state.with(POWER, 0));
    }

}
