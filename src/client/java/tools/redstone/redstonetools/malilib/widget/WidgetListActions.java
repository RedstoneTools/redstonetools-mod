package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;

import javax.annotation.Nullable;
import java.util.Collection;

public class WidgetListActions extends WidgetListBase<CommandActionBase, WidgetActionEntry> {
	private final MacroBase macroBase;

	public WidgetListActions(int x, int y, int width, int height, float zLevel,
	                         @Nullable ISelectionListener<CommandActionBase> selectionListener, MacroBase macroBase) {
		super(x, y, width, height, selectionListener);
		this.macroBase = macroBase;
		this.browserEntryHeight = 22;
	}

	@Override
	protected Collection<CommandActionBase> getAllEntries() {
		return macroBase.actions;
	}

	@Override
	protected WidgetActionEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, CommandActionBase entry) {
		return new WidgetActionEntry(x, y, this.browserEntryWidth,
			this.getBrowserEntryHeightFor(entry), entry, listIndex, this);
	}
}
