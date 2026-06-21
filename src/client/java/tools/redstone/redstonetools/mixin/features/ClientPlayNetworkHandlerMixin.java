package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.ClientCommands;
import tools.redstone.redstonetools.utils.DependencyLookup;
import tools.redstone.redstonetools.utils.StringUtils;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "handleCommands", at = @At("RETURN"), order = 750)
	private void onOnCommandTree(ClientboundCommandsPacket packet, CallbackInfo info) {
		var parse = commands.parse("base 0x2 5", suggestionsProvider); // there's probably a better way to check for rst being on the server, but I cba rn
		DependencyLookup.REDSTONE_TOOLS_SERVER_PRESENT = !parse.getReader().canRead();
		ClientCommands.registerCommands(ClientCommandInternals.getActiveDispatcher(), CommandBuildContext.simple(this.registryAccess, this.enabledFeatures));
	}
	@Shadow
	private CommandDispatcher<SharedSuggestionProvider> commands;

	@Shadow
	@Final
	private ClientSuggestionProvider suggestionsProvider;

	@Final
	@Shadow
	private FeatureFlagSet enabledFeatures;

	@Final
	@Shadow
	private RegistryAccess.Frozen registryAccess;

	@ModifyVariable(method = "sendCommand", at = @At("HEAD"), argsOnly = true, order = 750)
	public String sendChatCommand(String command) {
		return StringUtils.insertVariablesAndMath(command);
	}
}
