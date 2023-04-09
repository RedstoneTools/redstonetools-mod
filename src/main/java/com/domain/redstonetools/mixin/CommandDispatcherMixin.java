package com.domain.redstonetools.mixin;

import com.domain.redstonetools.RedstoneToolsClient;
import com.domain.redstonetools.macros.WorldlessCommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
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
            RedstoneToolsClient.LOGGER.info("registered: " + command.getLiteral());
            WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher().register((LiteralArgumentBuilder<CommandSource>) command);
        }
    }

    @ModifyVariable(method = "getSmartUsage(Lcom/mojang/brigadier/tree/CommandNode;Ljava/lang/Object;)Ljava/util/Map;", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public Object getSmartUsageSource(Object source) {
        return getSourceOrDefault(source);
    }

   /* @Inject(method = "parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", at = @At("HEAD"), remap = false)
    public void inject(StringReader command, S source, CallbackInfoReturnable<ParseResults<S>> cir){

        RedstoneToolsClient.LOGGER.info("CALLED");
        RedstoneToolsClient.LOGGER.info("CALLED");
        RedstoneToolsClient.LOGGER.info("CALLED");
        RedstoneToolsClient.LOGGER.info("CALLED");

        RedstoneToolsClient.LOGGER.info(source.toString());
    }*/

    @ModifyVariable(method = "parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    public Object parseSource(Object source) {
        return getSourceOrDefault(source);
    }

    private Object getSourceOrDefault(Object def) {
        RedstoneToolsClient.LOGGER.info(def.toString());
        if (WorldlessCommandHelper.dummyNetworkHandler.getCommandDispatcher().equals(this)) {
            RedstoneToolsClient.LOGGER.info("equals woo");
            return WorldlessCommandHelper.commandSource;
        }

        return def;
    }


}
