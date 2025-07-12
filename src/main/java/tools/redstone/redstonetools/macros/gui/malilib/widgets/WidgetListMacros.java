package tools.redstone.redstonetools.macros.gui.malilib.widgets;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;

import javax.annotation.Nullable;

public class WidgetListMacros extends WidgetListBase<MacroBase, MacroEntry> {

	public WidgetListMacros(int x, int y, int width, int height,
	                        @Nullable ISelectionListener<MacroBase> selectionListener)
	{
		super(x, y, width, height, selectionListener);

		this.browserEntryHeight = 22;
	}

	@Override
	protected MacroEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, MacroBase entry)
	{
		return new MacroEntry(x, y, this.browserEntryWidth,
				this.getBrowserEntryHeightFor(entry), entry, listIndex, this);
	}
}
