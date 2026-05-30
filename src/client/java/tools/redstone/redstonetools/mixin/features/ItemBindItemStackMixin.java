package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.utils.ItemUtils;

@Mixin(ItemStack.class)
public abstract class ItemBindItemStackMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void checkCommandNBT(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		assert Minecraft.getInstance().player != null;
		ItemStack stack = user.getMainHandItem();
		if (stack.isEmpty()) stack = user.getOffhandItem();
		if (!world.isClientSide()) return;
		if (ItemUtils.containsCommand(stack)) {
			String command = ItemUtils.getCommand(stack);
			Minecraft.getInstance().player.connection.sendCommand(command);
			cir.setReturnValue(InteractionResult.PASS);
		}
	}
}