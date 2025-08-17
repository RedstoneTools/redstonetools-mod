package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.RedstoneTools;

@Mixin(ItemStack.class)
public abstract class ItemBindItemStackMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void checkCommandNBT(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		assert MinecraftClient.getInstance().player != null;
		ItemStack stack = user.getMainHandStack();
		if (stack.isEmpty()) stack = user.getOffHandStack();
		if (!world.isClient()) return;
		if (stack.contains(RedstoneTools.COMMAND_COMPONENT)) {
			String command = stack.getComponents().get(RedstoneTools.COMMAND_COMPONENT);
			MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}
