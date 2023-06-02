package tools.redstone.redstonetools.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Invoker("addBlockEntityNbt")
    ItemStack addBlockEntityNbt(ItemStack stack, BlockEntity blockEntity);
}
