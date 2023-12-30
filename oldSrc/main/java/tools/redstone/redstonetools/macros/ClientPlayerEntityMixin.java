package tools.redstone.redstonetools.macros;

import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface ClientPlayerEntityMixin {

    void setNetworkHandler(ClientPlayNetworkHandler networkHandler);

}
