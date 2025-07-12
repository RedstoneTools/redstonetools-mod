package tools.redstone.redstonetools.macros.gui.malilib;

import fi.dy.masa.malilib.gui.*;
import fi.dy.masa.malilib.util.*;
import tools.redstone.redstonetools.macros.gui.malilib.widgets.MacroBase;

public class GuiMacroEditor extends GuiBase {
	private final MacroBase command;

	public GuiMacroEditor(MacroBase command) {
		this.command = command;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int x = 10;
		int y = 20;

		this.createShapeEditorElements(x, y);
	}

	private void createShapeEditorElements(int x, int y)
	{
		this.addLabel(x, y, -1, 14, 0xFFFFFFFF, StringUtils.translate("minihud.gui.label.display_name_colon"));
		y += 12;

		GuiTextFieldGeneric textField = new GuiTextFieldGeneric(x, y, 240, 17, this.textRenderer);
		textField.setText("");
		this.addTextField(textField, (txtFld) -> { this.command.setName(txtFld.getText()); return true; });
		y += 20;

		int renderTypeX = x + 230;
		int renderTypeY = y + 2;
	}
}
