package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.MacroBase;
import tools.redstone.redstonetools.malilib.widget.WidgetListMacros;
import tools.redstone.redstonetools.malilib.widget.WidgetMacroEntry;

import java.util.ArrayList;

public class GuiMacroManager extends GuiListBase<MacroBase, WidgetMacroEntry, WidgetListMacros>
		implements ISelectionListener<MacroBase> {

	public GuiMacroManager() {
		super(10, 68);

		this.title = "Macro manager";
	}

	@Override
	protected int getBrowserWidth() {
		return this.width - 20;
	}

	@Override
	protected int getBrowserHeight() {
		return this.height - this.getListY() - 6;
	}

	@Override
	public void initGui() {
		GuiConfigs.tab = GuiConfigs.ConfigGuiTab.MACROS;

		super.initGui();

		this.clearWidgets();
		this.clearButtons();
		this.createTabButtons();
		this.getListWidget().refreshEntries();
	}

	protected void createTabButtons() {
		int x = 10;
		int y = 26;
		int rows = 1;

		for (GuiConfigs.ConfigGuiTab tab : GuiConfigs.ConfigGuiTab.values()) {
			int width = this.getStringWidth(tab.getDisplayName()) + 10;

			if (x >= this.width - width - 10) {
				x = 10;
				y += 22;
				++rows;
			}

			x += this.createTabButton(x, y, width, tab);
		}

		String name = "Add macro";
		ButtonGeneric addMacroButton = new ButtonGeneric(this.width - 10, y, -1, true, name);

		// Check if there is enough space to put the dropdown widget and
		// the button at the end of the last tab button row
		if (rows < 2 || (this.width - 10 - x < (addMacroButton.getWidth() + 4))) {
			y += 22;
		}

		addMacroButton.setY(y);

		this.setListPosition(this.getListX(), y + 20);
		this.reCreateListWidget();

		this.addButton(addMacroButton, (btn, mbtn) -> {
			MacroManager.addMacro(new MacroBase("test", "", new ArrayList<>()));
			this.getListWidget().refreshEntries();
		});
	}

	protected int createTabButton(int x, int y, int width, GuiConfigs.ConfigGuiTab tab) {
		ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
		button.setEnabled(GuiConfigs.tab != tab);
		this.addButton(button, new ButtonListenerTab(tab));

		return button.getWidth() + 2;
	}

	@Override
	protected WidgetListMacros createListWidget(int listX, int listY) {
		return new WidgetListMacros(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), 0, this);
	}

	@Override
	public void onSelectionChange(@Nullable MacroBase macroBase) {

	}

	public static class ButtonListenerTab implements IButtonActionListener {
		private final GuiConfigs.ConfigGuiTab tab;

		public ButtonListenerTab(GuiConfigs.ConfigGuiTab tab) {
			this.tab = tab;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
			GuiConfigs.tab = this.tab;
			GuiBase.openGui(new GuiConfigs());
		}
	}
}