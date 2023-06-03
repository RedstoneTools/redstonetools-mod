package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.features.commands.ItemBindFeature;


public abstract class ItemBindMixin {

    @Mixin(ItemStack.class)
    private abstract static class ItemStackMixin {

        @Shadow
        public abstract @Nullable NbtCompound getNbt();

        @Inject(method = "use", at = @At("HEAD"), cancellable = true)
        public void checkCommandNBT(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
            if (tryToExecuteNBTCommand(hand, world)) {
                cir.setReturnValue(TypedActionResult.pass((ItemStack) ((Object) this)));
            }
        }

        @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
        public void checkCommandNBT(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
            if (tryToExecuteNBTCommand(context.getHand(), context.getWorld())) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }

        private boolean tryToExecuteNBTCommand(Hand hand, World world) {
            if (hand == Hand.OFF_HAND || world.isClient) return false;
            NbtCompound nbt = getNbt();
            if (nbt == null || !nbt.contains("command")) return false;
            NbtString command = (NbtString) nbt.get("command");
            MinecraftClient.getInstance().player.sendChatMessage(command.asString());

            return true;
        }
    }

    @Mixin(ClientPlayerEntity.class)
    private abstract static class PlayerMixin {

        @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
        public void injectCommand(String message, CallbackInfo ci) {
            if (!message.startsWith("/") || !ItemBindFeature.waitingForCommand) return;

            ItemBindFeature.addCommand((ClientPlayerEntity) ((Object)this),message);
            ci.cancel();
        }
    }


}
