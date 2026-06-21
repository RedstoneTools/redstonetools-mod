/*
	This file is licenced under the MPL 2.0
	https://www.mozilla.org/en-US/MPL/2.0/

	Command from: https://github.com/SpiritGameStudios/Specter/blob/65c217ad4776dbbfacbe8bac8a3d4e9ccd9bdd30/specter-debug/src/main/java/dev/spiritstudios/specter/impl/debug/command/ComponentsCommand.java
 */

package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import tools.redstone.redstonetools.Commands;

import java.util.Objects;

public class ItemComponentsFeature {
	public static final ItemComponentsFeature INSTANCE = new ItemComponentsFeature();

	protected ItemComponentsFeature() {
	}

	private final SimpleCommandExceptionType NO_COMPONENTS_EXCEPTION = new SimpleCommandExceptionType(Component.literal("No components!"));

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
		dispatcher.register(
			net.minecraft.commands.Commands.literal("components")
				.requires(Commands.PERMISSION_LEVEL_2)
				.executes(context -> components(Objects.requireNonNull(context.getSource().getPlayer()).getMainHandItem(), context.getSource()))
				.then(net.minecraft.commands.Commands.argument("target", EntityArgument.player())
					.executes(context -> components(EntityArgument.getPlayer(context, "target").getMainHandItem(), context.getSource()))
				)
		);
	}

	private int components(ItemStack stack, CommandSourceStack source) throws CommandSyntaxException {
		DataComponentPatch components = stack.getComponentsPatch();
		if (components.isEmpty())
			throw NO_COMPONENTS_EXCEPTION.create();

		RegistryOps<Tag> ops = source.registryAccess().createSerializationContext(NbtOps.INSTANCE);
		Tag output = DataComponentPatch.CODEC.encodeStart(ops, components).getOrThrow();

		source.sendSuccess(() -> NbtUtils.toPrettyComponent(output), false);

		return Command.SINGLE_SUCCESS;
	}
}
