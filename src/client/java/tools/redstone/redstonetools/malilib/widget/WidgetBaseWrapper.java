package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
//? if >=1.21.11
import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//? if >=1.21.10 {
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//? }
import java.util.Collection;

public class WidgetBaseWrapper implements GuiEventListener, Renderable, NarratableEntry {
	private final WidgetBase wrapped;

	public WidgetBaseWrapper(WidgetBase wrapped) {
		this.wrapped = wrapped;
	}

	//? if <=1.21.8 {
	/*@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			if (configButtonKeybind.isMouseOver((int) mouseX, (int) mouseY)) {
				boolean selectedPre = configButtonKeybind.isSelected();
				configButtonKeybind.onMouseClicked((int) mouseX, (int) mouseY, button);
				if (!selectedPre) {
					configButtonKeybind.onSelected();
				}
				return true;
			} else if (configButtonKeybind.isSelected()) {
				configButtonKeybind.onClearSelection();
				return true;
			}
		}
		return wrapped.onMouseClicked((int) mouseX, (int) mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		wrapped.onMouseReleased((int) mouseX, (int) mouseY, button);
		return GuiEventListener.super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) configButtonKeybind.onKeyPressed(keyCode);
		return wrapped.onKeyTyped(keyCode, scanCode, modifiers);
	}
	@Override
	public boolean charTyped(char chr, int modifiers) {
		return wrapped.onCharTyped(chr, modifiers);
	}
	*///? } else {
	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
		if (this.wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			if (configButtonKeybind.isMouseOver((int) click.x(), (int) click.y())) {
				boolean selectedPre = configButtonKeybind.isSelected();
				configButtonKeybind.onMouseClicked(click, doubled);
				if (!selectedPre) {
					configButtonKeybind.onSelected();
				}
				return true;
			} else if (configButtonKeybind.isSelected()) {
				configButtonKeybind.onClearSelection();
				return true;
			}
		}
		return wrapped.onMouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		wrapped.onMouseReleased(click);
		return GuiEventListener.super.mouseReleased(click);
	}

	@Override
	public boolean charTyped(CharacterEvent input) {
		return wrapped.onCharTyped(input);
	}

	@Override
	public boolean keyPressed(KeyEvent input) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) configButtonKeybind.onKeyPressed(input.input());
		return wrapped.onKeyTyped(input);
	}
	//? }

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		return wrapped.onMouseScrolled((int) mouseX, (int) mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return wrapped.isMouseOver((int) mouseX, (int) mouseY);
	}

	@Override
	public void setFocused(boolean focused) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			if (focused) {
				configButtonKeybind.onSelected();
			} else {
				configButtonKeybind.onClearSelection();
			}
		}
	}

	@Override
	public boolean isFocused() {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) {
			return configButtonKeybind.isSelected();
		}
		return false;
	}

	//? if <26.1 {
	/*@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
		//? if <=1.21.5 {
		/^wrapped.render(mouseX, mouseY, this.isFocused(), context);
		^///? } else if <=1.21.10 {
		/^wrapped.render(context, mouseX, mouseY, this.isFocused());
		^///? } else {
		wrapped.render(GuiContext.fromGuiGraphics(context), mouseX, mouseY, this.isFocused());
		//? }
	}
	*///? } else {
	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		wrapped.render(GuiContext.fromGuiGraphics(graphics), mouseX, mouseY, this.isFocused());
	}
	//? }

	@Override
	public NarrationPriority narrationPriority() {
		if (this.isFocused()) return NarrationPriority.FOCUSED;
		return NarrationPriority.NONE;
	}

	@Override
	public Collection<? extends NarratableEntry> getNarratables() {
		return NarratableEntry.super.getNarratables();
	}

	@Override
	public void updateNarration(NarrationElementOutput builder) {

	}
}
