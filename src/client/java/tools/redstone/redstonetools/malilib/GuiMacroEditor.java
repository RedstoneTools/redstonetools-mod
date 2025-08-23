package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.MacroBase;
import tools.redstone.redstonetools.malilib.widget.WidgetListMacros;

import java.util.List;

public class GuiMacroEditor extends GuiConfigsBase {
	private final MacroBase macro;
	private final WidgetListMacros parent;
	private float errorCountDown;

	public GuiMacroEditor(MacroBase macro, WidgetListMacros parent) {
		super(10, 50, RedstoneTools.MOD_ID, null, macro.getName(), "");
		this.parent = parent;
		this.macro = macro;
		this.title = macro.getName();
		this.configEnabled = new ConfigBoolean("Enabled", this.macro.isEnabled(), "Whether or not to enable the macro");
		this.configName = new ConfigString("Name", this.macro.getName(), "Name of the macro");
	}

	@Override
	public void initGui() {
		super.initGui();

		int x = 10;

		ButtonGeneric button = new ButtonGeneric(x, this.height - 24, -1, 20, GuiConfigs.ConfigGuiTab.MACROS.getDisplayName());
		this.addButton(button, (a, b) -> updateConfigsAndClose());
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		super.render(drawContext, mouseX, mouseY, partialTicks);
		if (errorCountDown > 0.0f) {
			drawContext.drawText(this.textRenderer, "Name already exists!", mouseX, mouseY - 10, 0xFFFFFFFF, true);
			errorCountDown -= partialTicks;
		}
	}

	@Override
	public void closeGui(boolean showParent) {
		if (updateConfigsAndClose()) return;
		super.closeGui(showParent);
	}

	private boolean updateConfigsAndClose() {
		if (MacroManager.nameExists(this.configName.getStringValue(), this.macro)) {
			errorCountDown = 50.0f;
			return true;
		}
		this.macro.setName(this.configName.getStringValue());
		this.macro.setEnabled(this.configEnabled.getBooleanValue());
		MacroManager.saveChanges();
		this.parent.refreshEntries();
		GuiBase.openGui(new GuiMacroManager());
		return false;
	}

	private final ConfigBoolean configEnabled;
	private final ConfigString configName;

	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		List<? extends IConfigBase> configs = List.of(
				this.configEnabled,
				this.macro.hotkey,
				this.configName
		);
		return ConfigOptionWrapper.createFor(configs);
	}
}