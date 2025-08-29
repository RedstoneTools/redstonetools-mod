package tools.redstone.redstonetools.malilib.widget.macro;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import tools.redstone.redstonetools.malilib.GuiMacroManager;
import tools.redstone.redstonetools.malilib.config.MacroManager;

import javax.annotation.Nullable;
import java.util.Collection;

public class WidgetListMacros extends WidgetListBase<MacroBase, WidgetMacroEntry> {
	public GuiMacroManager parent;
	public WidgetListMacros(int x, int y, int width, int height, GuiMacroManager parent,
							@Nullable ISelectionListener<MacroBase> selectionListener) {
		super(x, y, width, height, selectionListener);
		this.parent = parent;
		this.browserEntryHeight = 22;
	}

	@Override
	protected Collection<MacroBase> getAllEntries() {
		return MacroManager.getAllMacros();
	}

	@Override
	protected WidgetMacroEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, MacroBase entry) {
		WidgetMacroEntry temp;
		try {
			temp = new WidgetMacroEntry8(x, y, this.browserEntryWidth,
					this.getBrowserEntryHeightFor(entry), isOdd, entry, listIndex, this);
		} catch (NoSuchMethodError ignored) {
			temp = new WidgetMacroEntry4(x, y, this.browserEntryWidth,
					this.getBrowserEntryHeightFor(entry), isOdd, entry, listIndex, this);
		}
		return temp;
	}
}
