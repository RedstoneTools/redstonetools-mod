package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.malilib.widget.*;

public class GuiMacroEditor2 extends GuiListBase<CommandActionBase, WidgetActionEntry, WidgetListActions>
	implements ISelectionListener<CommandActionBase> {

	private final MacroBase macro;

	public GuiMacroEditor2(MacroBase macro) {
		super(10, 10);

		this.macro = macro;
		this.title = "Macro editor";
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

		int y = 26;
		String name = "Add command";
		ButtonGeneric addCommandButton = new ButtonGeneric(this.width - 10, y, -1, true, name);

		this.setListPosition(this.getListX(), y + 20);
		this.reCreateListWidget();

		this.addButton(addCommandButton, (btn, mbtn) -> {
			this.macro.actions.add(new CommandActionBase(""));
			this.getListWidget().refreshEntries();
		});

		this.getListWidget().refreshEntries();
	}

	@Override
	protected WidgetListActions createListWidget(int listX, int listY) {
		return new WidgetListActions(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), 0, this, this.macro);
	}

	@Override
	public void onSelectionChange(@Nullable CommandActionBase commandActionBase) {

	}
}
