package net.fabricmc.example.registers;

import net.fabricmc.example.command.GreetCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.util.InputUtil;

public class CommandRegister {

    private CommandRegister() {
    }

    public static void registerCommands() {
        //register all commands here

        CommandRegistrationCallback.EVENT.register(GreetCommand::register);
        registerCommandKeyBind("key.category.modid.greet", "key.modid.greet", InputUtil.Type.KEYSYM, GreetCommand.command);
    }

    public static void registerCommandKeyBind(String keyCategoryTrans, String keyTrans, InputUtil.Type type, String command) {
        KeyBindRegister.registerKeyBind(keyCategoryTrans, keyTrans, type, client -> {
            if (client.player == null) return;
            client.player.sendChatMessage("/" + command);
        });
    }


}
