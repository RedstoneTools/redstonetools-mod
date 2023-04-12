package com.domain.redstonetools.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixinImpl implements com.domain.redstonetools.macros.ClientPlayerEntityMixin {

    @Mutable
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;


    public void setNetworkHandler(ClientPlayNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

}
