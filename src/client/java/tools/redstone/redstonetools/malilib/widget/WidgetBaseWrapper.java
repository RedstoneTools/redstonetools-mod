package tools.redstone.redstonetools.malilib.widget;

import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
//? if >=1.21.11
import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
//? if >1.21.8 {
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
//? }

import java.util.Collection;

public class WidgetBaseWrapper implements Element, Drawable, Selectable {
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
		return Element.super.mouseReleased(mouseX, mouseY, button);
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
	public boolean mouseClicked(Click click, boolean doubled) {
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
	public boolean mouseReleased(Click click) {
		wrapped.onMouseReleased(click);
		return Element.super.mouseReleased(click);
	}

	@Override
	public boolean charTyped(CharInput input) {
		return wrapped.onCharTyped(input);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (wrapped instanceof ConfigButtonKeybind configButtonKeybind) configButtonKeybind.onKeyPressed(input.getKeycode());
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

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		//? if <=1.21.5 {
		/*wrapped.render(mouseX, mouseY, this.isFocused(), context);
		*///? } else if <=1.21.10 {
		/*wrapped.render(context, mouseX, mouseY, this.isFocused());
		*///? } else {
		wrapped.render(GuiContext.fromGuiGraphics(context), mouseX, mouseY, this.isFocused());
		//? }
	}

	@Override
	public SelectionType getType() {
		if (this.isFocused()) return SelectionType.FOCUSED;
		return SelectionType.NONE;
	}

	@Override
	public Collection<? extends Selectable> getNarratedParts() {
		return Selectable.super.getNarratedParts();
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}
}
