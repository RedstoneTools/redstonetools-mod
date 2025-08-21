/*
	This file is licenced under the MPL 2.0
	https://www.mozilla.org/en-US/MPL/2.0/

	Command from: https://github.com/SpiritGameStudios/Specter/blob/65c217ad4776dbbfacbe8bac8a3d4e9ccd9bdd30/specter-debug/src/main/java/dev/spiritstudios/specter/impl/debug/command/ComponentsCommand.java
 */

package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;

import static net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT;

public class ItemComponentsFeature {
	private static final SimpleCommandExceptionType NO_COMPONENTS_EXCEPTION = new SimpleCommandExceptionType(Text.literal("No components!"));

	public static void registerCommand() {
		EVENT.register((dispatcher, registryAccess, enviroment) -> dispatcher.register(CommandManager.literal("components")
			.executes(context -> components(Objects.requireNonNull(context.getSource().getPlayer()).getMainHandStack(), context.getSource()))
			.then(CommandManager.argument("target", EntityArgumentType.player())
				.executes(context -> components(EntityArgumentType.getPlayer(context, "target").getMainHandStack(), context.getSource()))
			)
		));
	}

	private static int components(ItemStack stack, ServerCommandSource source) throws CommandSyntaxException {
		ComponentChanges components = stack.getComponentChanges();
		if (components.isEmpty())
			throw NO_COMPONENTS_EXCEPTION.create();

		RegistryOps<NbtElement> ops = source.getRegistryManager().getOps(NbtOps.INSTANCE);
		NbtElement output = ComponentChanges.CODEC.encodeStart(ops, components).getOrThrow();

		source.sendFeedback(() -> NbtHelper.toPrettyPrintedText(output), false);

		return Command.SINGLE_SUCCESS;
	}
}
