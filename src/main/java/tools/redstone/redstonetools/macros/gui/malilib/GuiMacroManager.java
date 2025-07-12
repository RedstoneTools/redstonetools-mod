package tools.redstone.redstonetools.macros.gui.malilib;

import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.macros.gui.malilib.widgets.MacroBase;
import tools.redstone.redstonetools.macros.gui.malilib.widgets.MacroEntry;
import tools.redstone.redstonetools.macros.gui.malilib.widgets.WidgetListMacros;

public class GuiMacroManager  extends GuiListBase<MacroBase, MacroEntry, WidgetListMacros>
		implements ISelectionListener<MacroBase> {
	public GuiMacroManager() {
		super(10, 68);

		this.title = "Macros";
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

		String name = "Add macro";
		ButtonGeneric addMacroButton = new ButtonGeneric(this.width - 10, y, -1, true, name);

		// Check if there is enough space to put the dropdown widget and
		// the button at the end of the last tab button row

		addMacroButton.setY(y);

		this.setListPosition(this.getListX(), y + 20);
		this.reCreateListWidget();
	}

	@Override
	protected WidgetListMacros createListWidget(int listX, int listY) {
		return new WidgetListMacros(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), this);
	}

	@Override
	public void onSelectionChange(@Nullable MacroBase entry) {
		// uhhhhh
	}
}