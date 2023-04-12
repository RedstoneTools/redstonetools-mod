package tools.redstone.redstonetools.mixin;

import tools.redstone.redstonetools.macros.WorldlessCommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@org.spongepowered.asm.mixin.Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin<S> {


    @Inject(method = "register", at =  @At("HEAD"), remap = false)
    private void register(LiteralArgumentBuilder<S> command, CallbackInfoReturnable<LiteralCommandNode<S>> cir) {
        if (!WorldlessCommandHelper.registered && !WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher().equals(this)){
            WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher().register((LiteralArgumentBuilder<CommandSource>) command);
        }
    }

    @ModifyVariable(method = "getSmartUsage(Lcom/mojang/brigadier/tree/CommandNode;Ljava/lang/Object;)Ljava/util/Map;", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public Object getSmartUsageSource(Object source) {
        return getSourceOrDefault(source);
    }

    @ModifyVariable(method = "parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public Object parseSource(Object source) {
        return getSourceOrDefault(source);
    }

    private Object getSourceOrDefault(Object def) {
        if (WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher().equals(this)) {
            return WorldlessCommandHelper.commandSource;
        }

        return def;
    }


}
