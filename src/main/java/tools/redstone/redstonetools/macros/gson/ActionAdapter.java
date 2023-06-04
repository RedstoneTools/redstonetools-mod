package tools.redstone.redstonetools.macros.gson;

import com.google.gson.*;
import tools.redstone.redstonetools.macros.actions.Action;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import java.lang.reflect.Type;

public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {
    @Override
    public JsonElement serialize(Action action, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        if (action instanceof CommandAction commandAction) {
            object.addProperty("type", "command");
            object.addProperty("command", commandAction.command);
        } else {
            throw new RuntimeException("Unknown action type: " + action.getClass().getName());
        }
        return object;
    }

    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String typeStr = object.get("type").getAsString();
        if (typeStr.equals("command")) {
            return new CommandAction(object.get("command").getAsString());
        }
        throw new RuntimeException("Unknown action type: " + typeStr);
    }
}
