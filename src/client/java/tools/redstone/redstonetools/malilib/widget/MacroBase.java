package tools.redstone.redstonetools.malilib.widget;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.macros.actions.CommandAction;

import java.util.ArrayList;
import java.util.List;

public class MacroBase {
	protected final MinecraftClient mc;

	protected String name;
	protected boolean enabled;
	public IKeybind keybind;
	public List<CommandAction> actions;

	transient protected boolean needsUpdate;

	public MacroBase(String name, String keybind, List<CommandAction> actions) {
		this.actions = actions;
		this.keybind = KeybindMulti.fromStorageString(keybind, KeybindSettings.DEFAULT);
		this.mc = MinecraftClient.getInstance();
		this.name = name;
		this.needsUpdate = true;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void toggleEnabled() {
		this.enabled = !this.enabled;

		if (this.enabled) {
			this.setNeedsUpdate();
		}
	}

	public void setNeedsUpdate() {
		this.needsUpdate = true;
	}

	public void moveToPosition(Vec3d pos) {
	}

	public List<String> getWidgetHoverLines() {
		List<String> lines = new ArrayList<>();

		lines.add(StringUtils.translate("minihud.gui.hover.shape.type_value", this.getName()));

		return lines;
	}

	public JsonObject toJson() {
		JsonObject obj = new JsonObject();

		obj.add("enabled", new JsonPrimitive(this.enabled));
		obj.add("display_name", new JsonPrimitive(this.name));

		return obj;
	}

	public void fromJson(JsonObject obj) {
		this.enabled = JsonUtils.getBoolean(obj, "enabled");
		if (JsonUtils.hasString(obj, "display_name")) {
			this.name = obj.get("display_name").getAsString();
		}
	}
}
