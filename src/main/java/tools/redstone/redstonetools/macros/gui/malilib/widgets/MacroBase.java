package tools.redstone.redstonetools.macros.gui.malilib.widgets;

public class MacroBase {
	protected String displayName;
	public MacroBase() {
		this.displayName = "Macro";
	}

	public String getName() {
		return this.displayName;
	}
	public void setName(String name) {
		this.displayName = name;
	}
}