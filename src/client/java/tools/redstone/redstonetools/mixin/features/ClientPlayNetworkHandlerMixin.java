package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tools.redstone.redstonetools.malilib.config.Configs;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@ModifyVariable(method = "sendChatCommand", at = @At("HEAD"), argsOnly = true)
	private String addArguments(String value) {
		System.out.println(value);
		ArrayList<String> spaces = new ArrayList<>();
		Collections.addAll(spaces, value.split(" "));
		switch (spaces.getFirst()) {
			case "airplace":
				System.out.println("airplace");
				try {
					spaces.get(1);
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(1, String.valueOf(Configs.General.PRESET_AIRPLACE_SHOW_OUTLINE.getBooleanValue()));
				}
			case "ssb":
				// uhh do this one later
				throw new NotImplementedException();
			case "quicktp":
				System.out.println("quicktp");
				try {
					spaces.get(1);
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_QUICKTP_DISTANCE.getDoubleValue()));
				}
				try {
					spaces.get(2);
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_QUICKTP_THROUGH_FLUIDS.getBooleanValue()));
				}
				try {
					spaces.get(3);
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add( String.valueOf(Configs.General.PRESET_QUICKTP_RESET_VELOCITY.getBooleanValue()));
				}
			case "/read":
				System.out.println("/read");
				try {
					System.out.println(spaces.get(1));
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_BINARYBLOCKREAD_OFFSET.getIntegerValue()));
				}
				try {
					System.out.println(spaces.get(2));
					System.out.println("/read");
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_BINARYBLOCKREAD_ONBLOCK.getStringValue()));
				}
				try {
					System.out.println(spaces.get(3));
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add( String.valueOf(Configs.General.PRESET_BINARYBLOCKREAD_TOBASE.getIntegerValue()));
				}
				try {
					System.out.println(spaces.get(4));
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add( String.valueOf(Configs.General.PRESET_BINARYBLOCKREAD_REVERSEBITS.getBooleanValue()));
				}
			case "/rstack":
				System.out.println("/rstack");
				try {
					System.out.println(spaces.get(1));
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_RSTACK_COUNT.getIntegerValue()));
				}
				try {
					System.out.println(spaces.get(3));
				} catch (IndexOutOfBoundsException ignored) {
					spaces.add(String.valueOf(Configs.General.PRESET_RSTACK_OFFSET.getStringValue()));
				}
		}
		StringBuilder valueBuilder = new StringBuilder();
		for (String str : spaces) valueBuilder.append(str).append(" ");
		value = valueBuilder.toString();
		value = value.strip();
		return value;
	}
}
