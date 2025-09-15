package tools.redstone.redstonetools.mixin.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
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

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onCommandTree", at = @At("RETURN"), order = 750)
	private void onOnCommandTree(CommandTreeS2CPacket packet, CallbackInfo info) {
		var parse = commandDispatcher.parse("base 0x2 5", commandSource); // there's probably a better way to check for rst being on the server, but I cba rn
		DependencyLookup.REDSTONE_TOOLS_SERVER_PRESENT = !parse.getReader().canRead();
		ClientCommands.registerCommands(ClientCommandInternals.getActiveDispatcher(), CommandRegistryAccess.of(this.combinedDynamicRegistries, this.enabledFeatures));
	}
	@Shadow
	private CommandDispatcher<CommandSource> commandDispatcher;

	@Shadow
	@Final
	private ClientCommandSource commandSource;

	@Final
	@Shadow
	private FeatureSet enabledFeatures;

	@Final
	@Shadow
	private DynamicRegistryManager.Immutable combinedDynamicRegistries;

	@ModifyVariable(method = "sendChatCommand", at = @At("HEAD"), argsOnly = true, order = 750)
	public String sendChatCommand(String command) {
		return StringUtils.insertVariablesAndMath(command);
	}
}
