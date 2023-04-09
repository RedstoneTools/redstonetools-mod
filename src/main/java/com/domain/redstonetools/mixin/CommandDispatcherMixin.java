package com.domain.redstonetools.mixin;

import com.domain.redstonetools.macros.gui.commandsuggestor.WorldlessCommandSuggestor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@org.spongepowered.asm.mixin.Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin<S> {


    @Inject(method = "register", at =  @At("HEAD"), remap = false)
    private void register(LiteralArgumentBuilder<S> command, CallbackInfoReturnable<LiteralCommandNode<S>> cir) {
        if (!WorldlessCommandSuggestor.registered && !WorldlessCommandSuggestor.dummyNetworkHandler.getCommandDispatcher().equals(this)){
            WorldlessCommandSuggestor.dummyNetworkHandler.getCommandDispatcher().register((LiteralArgumentBuilder<CommandSource>) command);
        }
    }

}
