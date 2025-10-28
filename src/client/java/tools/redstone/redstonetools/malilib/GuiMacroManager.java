package tools.redstone.redstonetools.malilib;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.malilib.widget.macro.MacroBase;
import tools.redstone.redstonetools.malilib.widget.macro.WidgetListMacros;
import tools.redstone.redstonetools.malilib.widget.macro.WidgetMacroEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GuiMacroManager extends GuiListBase<MacroBase, WidgetMacroEntry, WidgetListMacros>
		implements ISelectionListener<MacroBase> {

	public GuiMacroManager() {
		super(10, 68);
		this.title = "Macro manager";
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		if (this.client != null && this.client.world == null) this.renderPanoramaBackground(drawContext, partialTicks);
		//? if <=1.21.5 {
		this.applyBlur();
		//?} else {
		/*this.applyBlur(drawContext);
		 *///?}
		super.render(drawContext, mouseX, mouseY, partialTicks);
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
			String string = "macro ";
			string += MacroManager.getAllMacros().size();
			MacroManager.addMacroToTop(new MacroBase(string, "", new ArrayList<>()));
			MacroManager.saveChanges();
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
		return new WidgetListMacros(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), this, this);
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

	@Override
	public void onFilesDropped(List<Path> paths) {
		for (Path path : paths) {
			try {
				String fileName = path.getFileName().toString().toLowerCase();
				if (!fileName.endsWith(".txt"))
					continue;
				List<String> commands = Files.readAllLines(path);
				String name = fileName.substring(0, fileName.length()-4);
				MacroBase macro = MacroManager.createCommandMacro(name, commands.toArray(new String[]{}));
				MacroManager.addMacroToTop(macro);
				this.getListWidget().refreshEntries();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}