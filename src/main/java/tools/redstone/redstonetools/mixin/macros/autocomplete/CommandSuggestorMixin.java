package tools.redstone.redstonetools.mixin.macros.autocomplete;

import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.macros.gui.MaroCommandSuggestor;

import java.util.concurrent.CompletableFuture;


@Mixin(ChatInputSuggestor.class)
public class CommandSuggestorMixin{

    @Shadow @Final
    TextFieldWidget textField;
    @Shadow private @Nullable CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow @Final
    int maxSuggestionSize;


    @ModifyVariable(method = "showCommandSuggestions", at = @At("STORE"), ordinal = 1)
    public int suggestionWindXPos(int j){
        if (MaroCommandSuggestor.instance(this)) {
            Suggestions suggestions = this.pendingSuggestions.join();
            return this.textField.getCharacterX(suggestions.getRange().getStart())+4;
        }
        return j;
    }

    @ModifyVariable(method = "showCommandSuggestions", at = @At("STORE"), ordinal = 2)
    public int suggestionWindYPos(int k){
        if (MaroCommandSuggestor.instance(this)) {
            Suggestions suggestions = this.pendingSuggestions.join();

            int y = MaroCommandSuggestor.getY(this)-2;
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
    public int messageYPosa(int j) {
        if (MaroCommandSuggestor.instance(this)) {
            int y = MaroCommandSuggestor.getY(this);
            i++;
            return y - 12*(i-1)+43;
        }
        return j;
    }
}
