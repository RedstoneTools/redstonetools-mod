package com.domain.redstonetools.mixin;

import com.domain.redstonetools.macros.WorldlessCommandHelper;
import com.domain.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSuggestor;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.dimension.DimensionType.OVERWORLD_ID;


@Mixin(CommandSuggestor.class)
public class CommandSuggestorMixin{

    @Shadow @Final
    MinecraftClient client;
    @Shadow @Final
    TextFieldWidget textField;
    @Shadow private @Nullable CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow @Final
    int maxSuggestionSize;
    private final ClientPlayerEntity dummyPlayer = new ClientPlayerEntity(MinecraftClient.getInstance(),new ClientWorld(WorldlessCommandHelper.dummyNetworkHandler,new ClientWorld.Properties(Difficulty.EASY,false,false),null,new RegistryEntry.Direct<>(DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, OVERWORLD_ID, 0.0F)),0,0,null,null,true,0 ), WorldlessCommandHelper.dummyNetworkHandler, null,null,false,false);

    private ClientPlayerEntity player;


    @Inject(method = "refresh", at = @At("HEAD"))
    public void refreshHead(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {
            player = client.player;
            client.player = dummyPlayer;
        }
    }

    @Inject(method = "refresh", at = @At("TAIL"))
    public void refreshTail(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {
            client.player = player;
            player = null;
        }
    }

    @Inject(method = "showUsages", at = @At("HEAD"))
    public void showUsagesHead(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {
            player = client.player;
            client.player = dummyPlayer;
        }
    }

    @Inject(method = "showUsages", at = @At("TAIL"))
    public void showUsagesTail(CallbackInfo ci){
        if (WorldlessCommandSuggestor.instance(this)) {
            client.player = player;
            player = null;
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
