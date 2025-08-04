package tools.redstone.redstonetools.macros;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MacroManager extends AbstractFeature {
    private static Path macrosFilePath;
    private static List<Macro> macros;

    public MacroManager() {
        macrosFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("config")
                .resolve("redstonetools")
                .resolve("macros.json");


        JsonArray macrosJson = null;
        try {
            Files.createDirectories(macrosFilePath.getParent());
            if (Files.exists(macrosFilePath)) {
                var reader = Json.createReader(new FileReader(macrosFilePath.toFile()));
                macrosJson = reader.readArray();
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (macrosJson == null) {
            macros = new ArrayList<>();
            macros.addAll(getDefaultMacros());
        } else {
            macros = getMacrosFromJson(macrosJson);
        }
    }

    public static List<Macro> getMacros() {
        return macros;
    }

    public static Macro getMacro(String name) {
        for (Macro macro : macros) {
            if (macro.name.equals(name)) {
                return macro;
            }
        }

        return null;
    }

    public static boolean nameExists(String name, Macro exclude) {
        for (Macro macro : macros) {
            if (macro == exclude) continue;
            if (macro.name.equals(name)) return true;
        }
        return false;
    }

    public static void saveChanges() {
        try {
            Files.createDirectories(macrosFilePath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        var macrosJson = Json.createArrayBuilder();
        for (Macro macro : macros) {
            macrosJson.add(getMacroJson(macro));
        }

        try (var writer = Json.createWriter(new FileWriter(macrosFilePath.toFile()))) {
            writer.writeArray(macrosJson.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JsonObject getMacroJson(Macro macro) {
        var actionsJson = Json.createArrayBuilder();
        for (CommandAction action : macro.actions) {
            actionsJson.add(getActionJson(action));
        }

        return Json.createObjectBuilder()
                .add("name", macro.name)
                .add("enabled", macro.enabled)
                .add("key", macro.getKey().getTranslationKey())
                .add("actions", actionsJson)
                .build();
    }

    private static JsonObject getActionJson(CommandAction action) {
        if (action instanceof CommandAction commandAction) {
            return Json.createObjectBuilder()
                    .add("type", "command")
                    .add("command", commandAction.command)
                    .build();
        }

        throw new RuntimeException("Unknown action type: " + action.getClass().getName());
    }

    public static List<Macro> getDefaultMacros() {
        return List.of(
                createCommandMacro("redstoner", new String[] {
                        "/gamerule doTileDrops false",
                        "/gamerule doTraderSpawning false",
                        "/gamerule doWeatherCycle false",
                        "/gamerule doDaylightCycle false",
                        "/gamerule doMobSpawning false",
                        "/gamerule doContainerDrops false",
                        "/time set noon",
                        "/weather clear"
                }),
                createCommandMacro("airplace", new String[] {"/airplace"}),
                createCommandMacro("autodust", new String[] {"/autodust"}),
                createCommandMacro("bigdust", new String[] {"/bigdust 3"}),
                createCommandMacro("copystate", new String[] {"/copystate"}),
                createCommandMacro("itembind", new String[] {"/itembind"}),
                createCommandMacro("minsel", new String[] {"//minsel"}),
                createCommandMacro("quicktp", new String[] {"/quicktp"}),
                createCommandMacro("binaryblockread", new String[] {"//binaryblockread"}),
                createCommandMacro("rstack", new String[] {"//rstack"}),
                createCommandMacro("update", new String[] {"//update"})
        );
    }

    private static Macro createCommandMacro(String name, String[] commands) {
        var actions = new CommandAction[commands.length];
        for (int i = 0; i < commands.length; i++) {
            actions[i] = new CommandAction(commands[i]);
        }

        return new Macro(name, true, InputUtil.UNKNOWN_KEY, List.of(actions));
    }

    private static List<Macro> getMacrosFromJson(JsonArray macrosJson) {
        List<Macro> macros = new ArrayList<>();

        for (int i = 0; i < macrosJson.size(); i++) {
            macros.add(getMacroFromJson(macrosJson.getJsonObject(i)));
        }

        return macros;
    }

    private static Macro getMacroFromJson(JsonObject macroJson) {
        var name = macroJson.getString("name");
        var enabled = macroJson.getBoolean("enabled");
        var key = macroJson.getString("key");
        var actions = getActionsFromJson(macroJson.getJsonArray("actions"));

        return new Macro(name, enabled, InputUtil.fromTranslationKey(key), actions);
    }

    private static List<CommandAction> getActionsFromJson(JsonArray actionsJson) {
        List<CommandAction> actions = new ArrayList<>();

        for (int i = 0; i < actionsJson.size(); i++) {
            actions.add(getActionFromJson(actionsJson.getJsonObject(i)));
        }

        return actions;
    }

    private static CommandAction getActionFromJson(JsonObject actionJson) {
        var type = actionJson.getString("type");

        if ("command".equals(type)) {
            return new CommandAction(actionJson.getString("command"));
        }

        throw new RuntimeException("Unknown action type: " + type);
    }

    public static void updateMacroKeys() {
        for (Macro macro : macros) {
            macro.changeKeyBindingKeyCode();
        }
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen != null) return;
            for (Macro macro : macros) {
                if (!macro.enabled) continue;
                if (macro.getKey() == InputUtil.UNKNOWN_KEY) continue;
                long window = MinecraftClient.getInstance().getWindow().getHandle();
                boolean pressed = InputUtil.isKeyPressed(window, macro.getKey().getCode());
                if (pressed && !macro._wasPressed) {
                    macro.run();
                }
                macro._wasPressed = pressed;
            }
        });
    }
}
