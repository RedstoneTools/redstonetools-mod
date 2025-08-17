package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(ItemStack.class)
public abstract class ItemBindItemStackMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void checkCommandNBT(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		assert MinecraftClient.getInstance().player != null;
		ItemStack stack = user.getMainHandStack();
		if (stack.isEmpty()) stack = user.getOffHandStack();
		if (!world.isClient()) return;
		if (stack.contains(RedstoneTools.COMMAND_COMPONENT)) {
			// some fucknut decided to change intermediary mappings for ItemStack#get from 1.21.4 to 1.21.8, so we have to do this instead
			String command;
			// try-catch hell
			try {
				command = stack.get(RedstoneTools.COMMAND_COMPONENT);
			} catch (NoSuchMethodError ignored) {
				Method found;
				try {
					// prod env
					found = ComponentHolder.class.getMethod("method_57824", ComponentType.class); // EW
				} catch (NoSuchMethodException e) {
					//  devenv
					try {
						found = ComponentHolder.class.getMethod("get", ComponentType.class); // EW
					} catch (NoSuchMethodException ex) {
						throw new RuntimeException(ex);
					}
				}
				found.setAccessible(true);
				try {
					command = (String) found.invoke(stack, RedstoneTools.COMMAND_COMPONENT);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}