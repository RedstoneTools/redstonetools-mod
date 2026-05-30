package tools.redstone.redstonetools.mixin.accessor;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(GiveCommand.class)
public interface GiveCommandAccessor {
	@Invoker
	static int invokeGiveItem(final CommandSourceStack source, final ItemInput input, final Collection<ServerPlayer> players, final int count) throws CommandSyntaxException {
		throw new AssertionError();
	}
}
