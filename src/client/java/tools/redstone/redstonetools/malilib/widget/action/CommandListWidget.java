package tools.redstone.redstonetools.malilib.widget.action;

import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
//? if >=1.21.11 {
/*import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.client.gui.navigation.ScreenRectangle;
*///? }
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenDirection;
//? if >=1.21.10 {
/*import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
*///? }
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.config.option.ConfigMacro;
import tools.redstone.redstonetools.macros.actions.CommandAction;
import tools.redstone.redstonetools.malilib.GuiMacroEditor;
import tools.redstone.redstonetools.mixin.features.ChatInputSuggestorAccessor;
import tools.redstone.redstonetools.mixin.features.SuggestionWindowAccessor;
//? if >=1.21.10 {
/*import tools.redstone.redstonetools.mixin.features.TextFieldWidgetAccessor;
*///?}


public class CommandListWidget extends AbstractSelectionList<CommandListWidget.CommandEntry> {
	private final GuiMacroEditor parent;
	@Nullable
	private CommandSuggestions commandSuggester;
	private final ConfigMacro macro;

	public CommandListWidget(GuiMacroEditor parent, Minecraft mc, int width, int height, int y, int itemHeight, ConfigMacro macro) {
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
		if (this.getSelected() != null) {
			this.getSelected().commandWidget.setSuggestion(null);
		}
		super.setSelected(entry);
		if (entry == null) {
			this.commandSuggester = null;
			return;
		}
		//? if >=1.21.10
		//((TextFieldWidgetAccessor) entry.commandWidget).getFormatters().clear();
		this.commandSuggester = new CommandSuggestions(
			minecraft,
			this.parent,
			entry.commandWidget,
			minecraft.font,
			false,
			false,
			0,
			10,
			false,
			0xD0000000
		) {
			@Override
			public void updateCommandInfo() {
				if (minecraft == null) return;
				if (minecraft.getConnection() == null) return;
				super.updateCommandInfo();
			}

			@Override
			public void renderUsage(GuiGraphics context) {
				//? if >=1.21.8 {
				/*context.pose().pushMatrix();
				*///?} else
				context.pose().pushPose();
				var x = 0;
				var y = entry.commandWidget.getY() + 20 - 72;
				context.pose().translate(x, y/*? if <1.21.8 {*/, 0/*?}*/);
				super.renderUsage(context);
				//? if >=1.21.8 {
				/*context.pose().popMatrix();
				*///?} else
				context.pose().popPose();
			}
		};
		this.commandSuggester.setAllowSuggestions(true);
		this.commandSuggester.updateCommandInfo();
	}

	@Override
	public void setScrollAmount(double scrollY) {
		super.setScrollAmount(scrollY);
		recalculateAllActionsPositions();
		if (this.commandSuggester != null && this.getSelected() != null) {
			CommandSuggestions.SuggestionsList window = ((ChatInputSuggestorAccessor) this.commandSuggester).getSuggestions();
			if (window == null) return;
			Rect2i area = ((SuggestionWindowAccessor) window).getRect();
			area.setY(this.getSelected().commandWidget.getY() + 23);
		}
	}

	@Override
	public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigation) {
		if (this.getItemCount() == 0) {
			return null;
		}
		if (!this.isFocused()) {
			return ComponentPath.leaf(this);
		}
		if (!(navigation instanceof FocusNavigationEvent.ArrowNavigation(ScreenDirection navigationDirection))) {
			return super.nextFocusPath(navigation);
		}
		if (navigationDirection.getAxis() == ScreenAxis.HORIZONTAL) {
			return ComponentPath.path(this.getSelected(), this);
		}

		CommandEntry neighboringEntry = this.nextEntry(navigationDirection);
		if (neighboringEntry == null) {
			return ComponentPath.path(this.getSelected(), this);
		}

		return ComponentPath.path(neighboringEntry, this);
	}

	@Override
	public boolean mouseClicked(/*$ mouse_clicked_params {*/double mouseX, double mouseY, int button/*$}*/) {
		if (this.commandSuggester != null && this.commandSuggester.mouseClicked(/*? if <1.21.10 {*/mouseX, mouseY, button/*?} else {*//*click*//*?}*/)) return true;
		return super.mouseClicked(/*$ mouse_clicked_args {*/mouseX, mouseY, button/*$}*/);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.commandSuggester != null && this.commandSuggester.mouseScrolled(verticalAmount)) return true;
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(/*$ keyinput_params {*/int keyCode, int scanCode, int modifiers/*$}*/) {
		if (this.commandSuggester != null && this.commandSuggester.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/)) return true;
		return super.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
	}

	@Override
	protected void renderListItems(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
		super.renderListItems(context, mouseX, mouseY, deltaTicks);
		if (this.commandSuggester != null) {
			this.commandSuggester.render(context, mouseX, mouseY);
		}
	}

	@Override
	//? if >=1.21.10 {
	/*protected boolean entriesCanBeSelected() {
	*///?} else {
	protected boolean isSelectedItem(int index) {
	//?}
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
		//? if <=1.21.8 {
		this.centerScrollOn(this.getFirstElement());
		//? } else
		//this.centerScrollOn(this.getFirst());
		recalculateAllActionsPositions();
		if (this.commandSuggester != null) {
			this.commandSuggester.updateCommandInfo();
		}
	}

	private void recalculateAllActionsPositions() {
		int i = this.getY() - (int) this.scrollAmount();

		for (CommandEntry entry : this.children()) {
			entry.removeButton.setY(i + 6);
			entry.commandWidget.setY(i + 3);
			//? if <=1.21.8 {
			i += this.itemHeight;
			//? } else
			//i += this.defaultEntryHeight;
		}
	}

	//? if >=1.21.10 {
	/*private CommandEntry getFirst() {
		return this.children().getFirst();
	}
	*///?}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {

	}

	public class CommandEntry extends Entry<CommandEntry> {
		public final CommandAction command;
		private EditBox commandWidget;
		private ButtonBase removeButton;

		public CommandEntry(CommandAction command) {
			this.command = command;
			this.commandWidget = new EditBox(Minecraft.getInstance().font, CommandListWidget.this.getX() + 4 + 25, 0, CommandListWidget.this.getRowWidth() - 100, 26, Component.nullToEmpty(""));
			commandWidget.setMaxLength(256);
			commandWidget.setValue(command.command);
			commandWidget.setResponder(this::onCommandChanged);

			this.removeButton = new ButtonGeneric(0, 0, -1, 20, "Remove");
			this.removeButton.setX(CommandListWidget.this.getX() + CommandListWidget.this.getRowWidth() - this.removeButton.getWidth() - 10 + 25);
			this.removeButton.setActionListener((button, mouseButton) -> {
				CommandListWidget.this.macro.getActions().remove(CommandListWidget.this.children().indexOf(this));
				CommandListWidget.this.removeEntryFromTop(this);
				recalculateAllActionsPositions();
			});
		}

		private void onCommandChanged(String text) {
			command.command = text;
			if (CommandListWidget.this.commandSuggester != null) {
				CommandListWidget.this.commandSuggester.updateCommandInfo();
			}
		}

		@Override
		public void /*? if <=1.21.8 {*/render/*? } else {*//*renderContent*//*? }*/(GuiGraphics context, /*? if <1.21.10 {*/ int index, int argY, int argX, int entryWidth, int entryHeight, /*?}*/ int mouseX, int mouseY, boolean hovered, float tickProgress) {
			commandWidget.render(context, mouseX, mouseY, tickProgress);

			//? if <=1.21.5 {
			removeButton.render(mouseX, mouseY, removeButton.isMouseOver(), context);
			//?} else if <=1.21.10 {
			/*removeButton.render(context, mouseX, mouseY, removeButton.isMouseOver());
			*///?} else {
			/*GuiContext guiContext = GuiContext.fromGuiGraphics(context);
			ScreenRectangle last = context.scissorStack.peek();
			if (last != null) {
				guiContext.pushScissor(last);
			}
			removeButton.render(guiContext, mouseX, mouseY, removeButton.isMouseOver());
			*///?}
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
		public boolean mouseClicked(/*$ mouse_clicked_params {*/double mouseX, double mouseY, int button/*$}*/) {
			if (commandWidget.mouseClicked(/*$ mouse_clicked_args {*/mouseX, mouseY, button/*$}*/)) return true;
			if (removeButton.onMouseClicked(/*$ on_mouse_clicked_args {*/(int) mouseX, (int) mouseY, button/*$}*/)) return true;
			return false;
		}

		@Override
		public boolean mouseReleased(/*$ dragged_released_params {*/double mouseX, double mouseY, int button/*$}*/) {
			removeButton.onMouseReleased(/*$ on_released_args {*/(int) mouseX, (int) mouseY, button/*$}*/);
			return commandWidget.mouseReleased(/*$ dragged_released_args {*/mouseX, mouseY, button/*$}*/);
		}

		@Override
		public boolean mouseDragged(/*$ dragged_released_params {*/double mouseX, double mouseY, int button/*$}*/, double deltaX, double deltaY) {
			return commandWidget.mouseDragged(/*$ dragged_released_args {*/mouseX, mouseY, button/*$}*/, deltaX, deltaY);
		}

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
			if (commandWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) return true;
			var x = /*? if <1.21.10 {*/(int)/*?}*/mouseX;
			var y = /*? if <1.21.10 {*/(int)/*?}*/mouseY;
			if (removeButton.onMouseScrolled(x, y, horizontalAmount, verticalAmount)) return true;
			return false;
		}

		@Override
		public boolean keyPressed(/*$ keyinput_params {*/int keyCode, int scanCode, int modifiers/*$}*/) {
			if (commandWidget.keyPressed(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/)) return true;
			if (removeButton.onKeyTyped(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/)) return true;
			return false;
		}

		@Override
		public boolean keyReleased(/*$ keyinput_params {*/int keyCode, int scanCode, int modifiers/*$}*/) {
			return commandWidget.keyReleased(/*$ keyinput_args {*/keyCode, scanCode, modifiers/*$}*/);
		}

		@Override
		public boolean charTyped(/*$ charinput_params {*/char chr, int modifiers/*$}*/) {
			if (commandWidget.charTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
			if (removeButton.onCharTyped(/*$ charinput_args {*/chr, modifiers/*$}*/)) return true;
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
