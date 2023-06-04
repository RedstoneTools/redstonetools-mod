package tools.redstone.redstonetools.macros.gson;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.minecraft.client.util.InputUtil;
import tools.redstone.redstonetools.macros.Macro;
import tools.redstone.redstonetools.macros.actions.Action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MacroAdapter implements JsonSerializer<Macro>, JsonDeserializer<Macro> {
    @Override
    public JsonElement serialize(Macro macro, Type type, JsonSerializationContext context) {
        JsonObject macroJson = new JsonObject();
        macroJson.addProperty("name", macro.name);
        macroJson.addProperty("enabled", macro.enabled);
        macroJson.addProperty("key", macro.getKey().getTranslationKey());
        macroJson.add("actions", context.serialize(macro.actions, new TypeToken<ArrayList<Action>>(){}.getType()));
        return macroJson;
    }

    @Override
    public Macro deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        var macroJson = jsonElement.getAsJsonObject();
        var name = macroJson.get("name").getAsString();
        var enabled = macroJson.get("enabled").getAsBoolean();
        var key = InputUtil.fromTranslationKey(macroJson.get("key").getAsString());
        List<Action> actions = context.deserialize(macroJson.get("actions"), new TypeToken<ArrayList<Action>>(){}.getType());

        return new Macro(name, enabled, key, actions);
    }
}
