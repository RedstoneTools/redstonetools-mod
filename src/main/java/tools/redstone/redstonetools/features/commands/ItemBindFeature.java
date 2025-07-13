package tools.redstone.redstonetools.features.commands;


import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.utils.ItemUtils;

import static net.minecraft.server.command.CommandManager.literal;

public class ItemBindFeature extends AbstractFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("itembind")
				.executes(context -> new ItemBindFeature().execute(context))));
	}
    public static boolean waitingForCommand = false;
    private static ServerPlayerEntity player;

    protected int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
        player = source.getPlayer();
        waitingForCommand = true;

		source.sendMessage(Text.literal("Please run any command and hold the item you want the command be bound to"));
        return 1;
    }

    public static void addCommand(String command) throws CommandSyntaxException {
        if (!waitingForCommand || MinecraftClient.getInstance().getServer() == null) return;

        if (player == null || MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid()) != player) {
            waitingForCommand = false;
            return;
        }

        ItemStack mainHandStack = player.getMainHandStack();
        if (mainHandStack == null || mainHandStack.getItem() == Items.AIR) {
            throw new SimpleCommandExceptionType(Text.literal("You need to be holding an item!")).create();
        }

        MinecraftClient client = MinecraftClient.getInstance();
	    assert client.world != null;
	    RegistryWrapper.WrapperLookup registries = client.world.getRegistryManager();
        NbtCompound tag = (NbtCompound) mainHandStack.toNbt(registries);

        tag.putString("command", command);

        mainHandStack = ItemStack.fromNbt(registries, tag).orElse(mainHandStack);
//        mainHandStack.getOrCreateNbt().put("command", NbtString.of(command));
        ItemUtils.addExtraNBTText(mainHandStack,"Command");

        waitingForCommand = false;

		player.sendMessage(Text.literal("Successfully bound command: '%s' to this item (%s)!".formatted(command, mainHandStack.getItem())));

    }
}
