package net.fabricmc.example.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class GreetCommand {

    public static String command = "greet";
    private GreetCommand(){
    }


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal(command).executes(GreetCommand::run));
    }

    private static int run(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.sendChatMessage("Hello!");
        }
        return 1;
    }


}
