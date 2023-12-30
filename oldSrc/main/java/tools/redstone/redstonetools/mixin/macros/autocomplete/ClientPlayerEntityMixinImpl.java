package tools.redstone.redstonetools.mixin.macros.autocomplete;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import tools.redstone.redstonetools.macros.ClientPlayerEntityMixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixinImpl implements ClientPlayerEntityMixin {

    @Mutable
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;


    public void setNetworkHandler(ClientPlayNetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

}
