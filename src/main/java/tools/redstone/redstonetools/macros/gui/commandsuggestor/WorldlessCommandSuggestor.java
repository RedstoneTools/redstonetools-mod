package tools.redstone.redstonetools.macros.gui.commandsuggestor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.HashMap;

public class WorldlessCommandSuggestor extends CommandSuggestor {
    private static final HashMap<WorldlessCommandSuggestor,Integer> yMap =  new HashMap<>();



    public WorldlessCommandSuggestor(MinecraftClient client, Screen owner, TextFieldWidget textField, TextRenderer textRenderer, boolean slashOptional, boolean suggestingWhenEmpty, int y, int maxSuggestionSize, int color) {
        super(client, owner, textField, textRenderer, slashOptional, suggestingWhenEmpty, 0, maxSuggestionSize, false, color);
        yMap.put(this,y);

    }

    public void close(){
        yMap.remove(this);
    }

    public static boolean instance(Object object) {
        return object instanceof WorldlessCommandSuggestor;
    }

    public static int getY(Object object){
        return yMap.get(object);
    }


}
