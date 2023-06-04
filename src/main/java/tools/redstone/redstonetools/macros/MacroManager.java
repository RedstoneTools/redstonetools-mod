package tools.redstone.redstonetools.macros;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class MacroManager {
    private final Path macrosFilePath;
    private final List<Macro> macros;

    public MacroManager() {
        this.macros = new ArrayList<>();

        try {
            macrosFilePath = MinecraftClient.getInstance().runDirectory.toPath()
                    .resolve("config")
                    .resolve("redstonetools")
                    .resolve("macros.json");

            Files.createDirectories(macrosFilePath.getParent());
            if (Files.exists(macrosFilePath)) {
                Macro[] parsedMacros = RedstoneToolsClient.GSON.fromJson(
                        Files.newBufferedReader(macrosFilePath),
                        new TypeToken<Macro[]>() {
                        }.getType()
                );
                if (parsedMacros != null) {
                    Collections.addAll(this.macros, parsedMacros);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Couldn't load macros config", t);
        }

        if (this.macros.isEmpty()) {
            this.macros.addAll(getDefaultMacros());
            saveChanges();
        }
    }

    public List<Macro> getMacros() {
        return macros;
    }

    public Macro getMacro(String name) {
        for (Macro macro : macros) {
            if (macro.name.equals(name)) {
                return macro;
            }
        }

        return null;
    }

    public void addMacro(Macro macro) {
        macros.add(macro);

        saveChanges();
    }

    public void removeMacro(Macro macro) {
        macros.remove(macro);

        saveChanges();
    }

    public void saveChanges() {
        // Write %appdata%/.minecraft/config/redstonetools/macros.json
        try {
            Files.createDirectories(macrosFilePath.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.writeString(macrosFilePath, RedstoneToolsClient.GSON.toJson(this.macros));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private List<Macro> getDefaultMacros() {
        return List.of(
                createCommandMacro("redstoner", new String[]{
                        "/gamerule doTileDrops false",
                        "/gamerule doTraderSpawning false",
                        "/gamerule doWeatherCycle false",
                        "/gamerule doDaylightCycle false",
                        "/gamerule doMobSpawning false",
                        "/gamerule doContainerDrops false",
                        "/time set noon",
                })
        );
    }

    private Macro createCommandMacro(String name, String[] commands) {
        var actions = new Action[commands.length];
        for (int i = 0; i < commands.length; i++) {
            actions[i] = new CommandAction(commands[i]);
        }

        return new Macro(name, true, InputUtil.UNKNOWN_KEY, List.of(actions));
    }

    public void updateMacroKeys() {
        for (Macro macro : macros) {
            macro.changeKeyBindingKeyCode();
        }
    }
}
