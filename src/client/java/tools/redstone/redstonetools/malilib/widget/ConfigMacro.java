package tools.redstone.redstonetools.malilib.widget;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fi.dy.masa.malilib.config.IConfigStringList;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public class ConfigMacro extends ConfigBooleanHotkeyed implements IConfigStringList {
	public static final Codec<ConfigMacro> CODEC = RecordCodecBuilder.create(
			(instance) ->
					instance.group(PrimitiveCodec.STRING.fieldOf("name")
											.forGetter(ConfigBase::getName),
									PrimitiveCodec.BOOL.fieldOf("defaultEnabled")
											.forGetter(ConfigBoolean::getDefaultBooleanValue),
									PrimitiveCodec.BOOL.fieldOf("valueEnabled")
											.forGetter(ConfigBoolean::getBooleanValue),
									PrimitiveCodec.STRING.fieldOf("defaultHotkey").forGetter((get) ->
											get.keybind.getDefaultStringValue()),
									KeybindSettings.CODEC.fieldOf("keybindSettings")
											.forGetter((get) ->
													get.keybind.getSettings()),
									PrimitiveCodec.STRING.fieldOf("comment")
											.forGetter((get) -> get.comment),
									PrimitiveCodec.STRING.fieldOf("prettyName")
											.forGetter((get) -> get.prettyName),
									PrimitiveCodec.STRING.fieldOf("translatedName").forGetter((get) ->
											get.translatedName),
									Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("defaultStrings").forGetter((get) ->
											get.defaultStrings.stream().toList()),
									Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("valueStrings").forGetter((get) -> get.strings))
							.apply(instance, (name, defaultValue, value, defaultHotkey, keybindSettings, comment, prettyName, translatedName, defaultStrings, valueStrings) ->
									new ConfigMacro(
											name,
											defaultValue,
											value,
											defaultHotkey,
											keybindSettings,
											comment,
											prettyName,
											translatedName,
											ImmutableList.copyOf(defaultStrings),
											valueStrings
									)));
	private final List<String> strings;
	private final ImmutableList<String> defaultStrings;

	public ConfigMacro(String name,
	                   boolean defaultEnabled,
	                   boolean valueEnabled,
	                   String defaultHotkey,
	                   KeybindSettings keybindSettings,
	                   String comment,
	                   String prettyName,
	                   String translatedName,
	                   ImmutableList<String> defaultStrings,
	                   List<String> valueStrings) {
		super(name, defaultEnabled, defaultHotkey, keybindSettings, comment, prettyName, translatedName);
		this.setBooleanValue(valueEnabled);
		this.strings = valueStrings;
		this.defaultStrings = defaultStrings;
	}

	public List<String> getStrings() {
		return this.strings;
	}

	public ImmutableList<String> getDefaultStrings() {
		return this.defaultStrings;
	}

	public void setStrings(List<String> strings) {
		if (!this.strings.equals(strings)) {
			this.strings.clear();
			this.strings.addAll(strings);
			this.onValueChanged();
		}

	}

	public void setModified() {
		this.onValueChanged();
	}

	public void resetToDefault() {
		this.setStrings(this.defaultStrings);
	}

	public boolean isModified() {
		return !this.strings.equals(this.defaultStrings);
	}

	public void setValueFromJsonElement(JsonElement element) {
//		super.setValueFromJsonElement(element);
//		this.strings.clear();
//
//		try {
//			if (element.isJsonArray()) {
//				JsonArray arr = element.getAsJsonArray();
//				int count = arr.size();
//
//				for(int i = 0; i < count; ++i) {
//					this.strings.add(arr.get(i).getAsString());
//				}
//			} else {
//				MaLiLib.LOGGER.warn("Failed to set config valueEnabled for '{}' from the JSON element '{}'", this.getName(), element);
//			}
//		} catch (Exception e) {
//			MaLiLib.LOGGER.warn("Failed to set config valueEnabled for '{}' from the JSON element '{}'", this.getName(), element, e);
//		}

	}

	public JsonElement getAsJsonElement() {
//		super.getAsJsonElement();
		JsonArray arr = new JsonArray();

//		for(String str : this.strings) {
//			arr.add(new JsonPrimitive(str));
//		}

		return arr;
	}
}
