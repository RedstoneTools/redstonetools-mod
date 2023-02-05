package net.fabricmc.example.registers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindRegister {

    public static void registerKeyBind(String keyCategoryTrans, String keyTrans, InputUtil.Type type, KeyBindPressConsumer consumer ) {
        registerKeyBind(keyCategoryTrans, keyTrans, type, -1, consumer);
    }
    public static void registerKeyBind(String keyCategoryTrans, String keyTrans, InputUtil.Type type, int code, KeyBindPressConsumer consumer ) {
        KeyBinding key = KeyBindingHelper.registerKeyBinding(new KeyBinding(keyTrans,type,code, keyCategoryTrans));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (key.wasPressed()) {
                consumer.pressed(client);
            }
        });
    }
    public interface KeyBindPressConsumer {
        void pressed(MinecraftClient minecraftClient);
    }

}
