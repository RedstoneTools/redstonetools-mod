package tools.redstone.redstonetools.malilib.widget.action;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
//? if >=1.21.11 {
import fi.dy.masa.malilib.render.GuiContext;
//?}
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationAxis;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
/*$ click_and_inputs_imports {*///
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.CharInput;/*$}*/
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.config.option.ConfigMacro;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.mixin.features.ChatInputSuggestorAccessor;
import tools.redstone.redstonetools.mixin.features.SuggestionWindowAccessor;
//? if >=1.21.10 {
import tools.redstone.redstonetools.mixin.features.TextFieldWidgetAccessor;
//?}


public class CommandListWidget extends EntryListWidget<CommandListWidget.CommandEntry> {
	private final GuiMacroEditor parent;
	@Nullable
	private ChatInputSuggestor commandSuggester;
	private final ConfigMacro macro;

	public CommandListWidget(GuiMacroEditor parent, MinecraftClient mc, int width, int height, int y, int itemHeight, ConfigMacro macro) {
		super(mc, width, height, y, itemHeight);
		this.parent = parent;
		this.macro = macro;
		for (CommandAction commandAction : this.macro.getActions()) {
			CommandEntry entry = new CommandEntry(commandAction);
			this.addEntry(entry);
			this.setSelected(entry);
		}
		this.recalculateAllActionsPositions();
		this.setSelected(null);
	}

	@Override
	public void setSelected(@Nullable CommandListWidget.CommandEntry entry) {
		if (this.getSelectedOrNull() != null) {
			this.getSelectedOrNull().commandWidget.setSuggestion(null);
		}
		super.setSelected(entry);
		if (entry == null) {
			this.commandSuggester = null;
			return;
		}
		//? if >=1.21.10
		((TextFieldWidgetAccessor) entry.commandWidget).getFormatters().clear();
		this.commandSuggester = new ChatInputSuggestor(
			client,
			this.parent,
			entry.commandWidget,
			client.textRenderer,
			false,
			false,
			0,
			10,
			false,
			0xD0000000
		) {
			@Override
			public void refresh() {
				if (client == null) return;
				if (client.getNetworkHandler() == null) return;
				super.refresh();
			}

			@Override
			public void renderMessages(DrawContext context) {
				//? if >=1.21.8 {
				context.getMatrices().pushMatrix();
				//?} else
				//context.getMatrices().push();
				var x = 0;
				var y = entry.commandWidget.getY() + 20 - 72;
				context.getMatrices().translate(x, y/*? if <1.21.8 {*//*, 0*//*?}*/);
				super.renderMessages(context);
				//? if >=1.21.8 {
				context.getMatrices().popMatrix();
				//?} else
				//context.getMatrices().pop();
			}
		};
		this.commandSuggester.setWindowActive(true);
		this.commandSuggester.refresh();
	}

	@Override
	public void setScrollY(double scrollY) {
		super.setScrollY(scrollY);
		recalculateAllActionsPositions();
		if (this.commandSuggester != null && this.getSelectedOrNull() != null) {
			ChatInputSuggestor.SuggestionWindow window = ((ChatInputSuggestorAccessor) this.commandSuggester).getWindow();
			if (window == null) return;
			Rect2i area = ((SuggestionWindowAccessor) window).getArea();
			area.setY(this.getSelectedOrNull().commandWidget.getY() + 23);
		}
	}

	@Override
	public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
		if (this.getEntryCount() == 0) {
			return null;
		}
		if (!this.isFocused()) {
			return GuiNavigationPath.of(this);
		}
		if (!(navigation instanceof GuiNavigation.Arrow(NavigationDirection navigationDirection))) {
			return super.getNavigationPath(navigation);
		}
		if (navigationDirection.getAxis() == NavigationAxis.HORIZONTAL) {
			return GuiNavigationPath.of(this.getSelectedOrNull(), this);
		}

		CommandEntry neighboringEntry = this.getNeighboringEntry(navigationDirection);
		if (neighboringEntry == null) {
			return GuiNavigationPath.of(this.getSelectedOrNull(), this);
		}

		return GuiNavigationPath.of(neighboringEntry, this);
	}

	@Override
	public boolean mouseClicked(/*$ mouse_clicked_params {*/Click click, boolean doubleClick/*$}*/) {
		if (this.commandSuggester != null && this.commandSuggester.mouseClicked(/*? if <1.21.10 {*//*mouseX, mouseY, button*//*?} else {*/click/*?}*/)) return true;
		return super.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.commandSuggester != null && this.commandSuggester.mouseScrolled(verticalAmount)) return true;
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params {*/KeyInput input/*$}*/) {
		if (this.commandSuggester != null && this.commandSuggester.keyPressed(/*$ keyinput_args {*/input/*$}*/)) return true;
		return super.keyPressed(/*$ keyinput_args {*/input/*$}*/);
	}

	@Override
	protected void renderList(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.renderList(context, mouseX, mouseY, deltaTicks);
		if (this.commandSuggester != null) {
			this.commandSuggester.render(context, mouseX, mouseY);
		}
	}

	@Override
	//? if >=1.21.10 {
	protected boolean isEntrySelectionAllowed() {
	//?} else {
	/*protected boolean isSelectedEntry(int index) {
	*///?}
		return false;
	}


	@Override
	public int getRowWidth() {
		return this.width - 50;
	}

	public void addEntry() {
		this.macro.getActions().addFirst(new CommandAction(""));
		CommandEntry entry = new CommandEntry(this.macro.getActions().getFirst());
		this.addEntryToTop(entry);
		this.centerScrollOn(this.getFirst());
		recalculateAllActionsPositions();
		if (this.commandSuggester != null) {
			this.commandSuggester.refresh();
		}
	}

	private void recalculateAllActionsPositions() {
		int i = this.getY() - (int) this.getScrollY();

		for (CommandEntry entry : this.children()) {
			entry.removeButton.setY(i + 6);
			entry.commandWidget.setY(i + 3);
			i += this.itemHeight;
		}
	}

	//? if >=1.21.10 {
	private CommandEntry getFirst() {
		return this.children().getFirst();
	}
	//?}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}

	public class CommandEntry extends Entry<CommandEntry> {
		public final CommandAction command;
		private TextFieldWidget commandWidget;
		private ButtonBase removeButton;

		public CommandEntry(CommandAction command) {
			this.command = command;
			this.commandWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, CommandListWidget.this.getX() + 4 + 25, 0, CommandListWidget.this.getRowWidth() - 100, 26, Text.of(""));
			commandWidget.setMaxLength(256);
			commandWidget.setText(command.command);
			commandWidget.setChangedListener(this::onCommandChanged);

			this.removeButton = new ButtonGeneric(0, 0, -1, 20, "Remove");
			this.removeButton.setX(CommandListWidget.this.getX() + CommandListWidget.this.getRowWidth() - this.removeButton.getWidth() - 10 + 25);
			this.removeButton.setActionListener((button, mouseButton) -> {
				CommandListWidget.this.macro.getActions().remove(CommandListWidget.this.children().indexOf(this));
				CommandListWidget.this.removeEntryWithoutScrolling(this);
				recalculateAllActionsPositions();
			});
		}

		private void onCommandChanged(String text) {
			command.command = text;
			if (CommandListWidget.this.commandSuggester != null) {
				CommandListWidget.this.commandSuggester.refresh();
			}
		}

		@Override
		public void render(DrawContext context, /*? if <1.21.10 {*/ /*int index, int argY, int argX, int entryWidth, int entryHeight, *//*?}*/ int mouseX, int mouseY, boolean hovered, float tickProgress) {
			commandWidget.render(context, mouseX, mouseY, tickProgress);

			//? if <=1.21.5 {
			/*removeButton.render(mouseX, mouseY, removeButton.isMouseOver(), context);
			*///?} else if <=1.21.10 {
			/*removeButton.render(context, mouseX, mouseY, removeButton.isMouseOver());
			*///?} else {
			GuiContext guiContext = GuiContext.fromGuiGraphics(context);
			ScreenRect last = context.scissorStack.peekLast();
			if (last != null) {
				guiContext.pushScissor(last);
			}
			removeButton.render(guiContext, mouseX, mouseY, removeButton.isMouseOver());
			//?}
		}

		@Override
		public void setFocused(boolean focused) {
			super.setFocused(focused);
			commandWidget.setFocused(focused);
		}

		@Override
		public void mouseMoved(double mouseX, double mouseY) {
			super.mouseMoved(mouseX, mouseY);
			commandWidget.mouseMoved(mouseX, mouseY);
		}

		@Override
		public boolean mouseClicked(/*$ mouse_clicked_params {*/Click click, boolean doubleClick/*$}*/) {
			if (commandWidget.mouseClicked(/*$ mouse_clicked_args {*/click, doubleClick/*$}*/)) return true;
			if (removeButton.onMouseClicked(/*$ on_mouse_clicked_args {*/click, doubleClick/*$}*/)) return true;
			return false;
		}

		@Override
		public boolean mouseReleased(/*$ dragged_released_params {*/Click click/*$}*/) {
			removeButton.onMouseReleased(/*$ on_released_args {*/click/*$}*/);
			return commandWidget.mouseReleased(/*$ dragged_released_args {*/click/*$}*/);
		}

		@Override
		public boolean mouseDragged(/*$ dragged_released_params {*/Click click/*$}*/, double deltaX, double deltaY) {
			return commandWidget.mouseDragged(/*$ dragged_released_args {*/click/*$}*/, deltaX, deltaY);
		}

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
			if (commandWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) return true;
			var x = /*? if <1.21.10 {*//*(int)*//*?}*/mouseX;
			var y = /*? if <1.21.10 {*//*(int)*//*?}*/mouseY;
			if (removeButton.onMouseScrolled(x, y, horizontalAmount, verticalAmount)) return true;
			return false;
		}

		@Override
		public boolean keyPressed(/*$ keyinput_params {*/KeyInput input/*$}*/) {
			if (commandWidget.keyPressed(/*$ keyinput_args {*/input/*$}*/)) return true;
			if (removeButton.onKeyTyped(/*$ keyinput_args {*/input/*$}*/)) return true;
			return false;
		}

		@Override
		public boolean keyReleased(/*$ keyinput_params {*/KeyInput input/*$}*/) {
			return commandWidget.keyReleased(/*$ keyinput_args {*/input/*$}*/);
		}

		@Override
		public boolean charTyped(/*$ charinput_params {*/CharInput input/*$}*/) {
			if (commandWidget.charTyped(/*$ charinput_args {*/input/*$}*/)) return true;
			if (removeButton.onCharTyped(/*$ charinput_args {*/input/*$}*/)) return true;
			return false;
		}

		@Override
		public boolean isMouseOver(double mouseX, double mouseY) {
			if (commandWidget.isMouseOver(mouseX, mouseY)) return true;
			if (removeButton.isMouseOver((int) mouseX, (int) mouseY)) return true;
			return false;
		}
	}
}
