package com.domain.redstonetools.macros;

import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface IClientPlayerEntityMixin {

    void setNetworkHandler(ClientPlayNetworkHandler networkHandler);

}
