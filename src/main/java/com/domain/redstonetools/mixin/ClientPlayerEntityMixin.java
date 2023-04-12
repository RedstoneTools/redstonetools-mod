package com.domain.redstonetools.mixin;

import com.domain.redstonetools.macros.IClientPlayerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements IClientPlayerEntityMixin {

    @Mutable
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;


    public void setNetworkHandler(ClientPlayNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

}
