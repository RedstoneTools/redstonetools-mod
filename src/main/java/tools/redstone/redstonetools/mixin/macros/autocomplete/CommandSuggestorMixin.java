package tools.redstone.redstonetools.mixin.macros.autocomplete;

import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.macros.ClientPlayerEntityMixin;
import tools.redstone.redstonetools.macros.WorldlessCommandHelper;
import tools.redstone.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSuggestor;

import java.util.concurrent.CompletableFuture;


@Mixin(CommandSuggestor.class)
public class CommandSuggestorMixin{

    @Shadow @Final
    MinecraftClient client;
    @Shadow @Final
    TextFieldWidget textField;
    @Shadow private @Nullable CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow @Final
    int maxSuggestionSize;


    private ClientPlayNetworkHandler networkHandler;



    @Inject(method = "refresh", at = @At("HEAD"))
    public void refreshHead(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {
            if (client.player == null || client.player.equals(WorldlessCommandHelper.dummyPlayer)) {
                client.player = WorldlessCommandHelper.dummyPlayer;
            } else {
                if (client.player.networkHandler != WorldlessCommandHelper.dummyNetworkHandler) networkHandler = client.player.networkHandler;
                ((ClientPlayerEntityMixin)client.player).setNetworkHandler(WorldlessCommandHelper.dummyNetworkHandler);
            }
        }
    }

    @Inject(method = "refresh", at = @At("TAIL"))
    public void refreshTail(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {

            if (client.player == null || client.player.equals(WorldlessCommandHelper.dummyPlayer)) {
                client.player = null;
            } else if (networkHandler != null){
                ((ClientPlayerEntityMixin)client.player).setNetworkHandler(networkHandler);
            }
        }
    }

    @Inject(method = "showUsages", at = @At("HEAD"))
    public void showUsagesHead(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {

            if (client.player == null || client.player.equals(WorldlessCommandHelper.dummyPlayer)) {
                client.player = WorldlessCommandHelper.dummyPlayer;
            } else {
                if (client.player.networkHandler != WorldlessCommandHelper.dummyNetworkHandler) networkHandler = client.player.networkHandler;
                ((ClientPlayerEntityMixin)client.player).setNetworkHandler(WorldlessCommandHelper.dummyNetworkHandler);
            }

        }
    }

    @Inject(method = "showUsages", at = @At("TAIL"))
    public void showUsagesTail(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {

            if (client.player == null || client.player.equals(WorldlessCommandHelper.dummyPlayer) ) {
                client.player = null;
            } else if (networkHandler != null){
                ((ClientPlayerEntityMixin)client.player).setNetworkHandler(networkHandler);
            }

        }
    }

    @ModifyVariable(method = "showSuggestions", at = @At("STORE"), ordinal = 1)
    public int suggestionWindXPos(int j){
        if (WorldlessCommandSuggestor.instance(this)) {
            Suggestions suggestions = this.pendingSuggestions.join();
            return this.textField.getCharacterX(suggestions.getRange().getStart())+4;
        }
        return j;
    }

    @ModifyVariable(method = "showSuggestions", at = @At("STORE"), ordinal = 2)
    public int suggestionWindYPos(int k){
        if (WorldlessCommandSuggestor.instance(this)) {
            Suggestions suggestions = this.pendingSuggestions.join();

            int y = WorldlessCommandSuggestor.getY(this)-2;
            return y +20 - Math.min(suggestions.getList().size(), this.maxSuggestionSize) * 12;
        }
        return k;
    }


    private int i = 0;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci){
        i = 0;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 3)
    public int messageYPos(int j) {
        if (WorldlessCommandSuggestor.instance(this)) {
            int y = WorldlessCommandSuggestor.getY(this);
            i++;
            return y - 12*(i-1)+43;
        }
        return j;
    }




}
