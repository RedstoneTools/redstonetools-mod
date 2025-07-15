package tools.redstone.redstonetools.macros.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.util.List;

public class StringListWidget extends ElementListWidget<StringListWidget.Entry> {
	private final List<String> backing;
	private final List<String> defaults;

	public StringListWidget(int width, int height, int top, int bottom, List<String> backing) {
		super(MinecraftClient.getInstance(), width, height, top, bottom, 20);
		this.backing = backing;
		this.defaults = List.copyOf(backing);
		rebuild();
	}

	public void addEmptyRow() {
		backing.add("");
		rebuild();
	}

	public void rebuild() {
		clearEntries();
		for (int i = 0; i < backing.size(); i++) {
			addEntry(new Entry(i));
		}
	}

	public void reset() {
		backing.clear();
		backing.addAll(defaults);
		rebuild();
	}

	@Override public int getRowWidth() { return this.width - 20; }

	class Entry extends ElementListWidget.Entry<Entry> {
		private final int idx;
		private final TextFieldWidget field;

		Entry(int idx) {
			this.idx = idx;
			this.field = new TextFieldWidget(
					MinecraftClient.getInstance().textRenderer,
					0,0, getRowWidth(), 20,
					Text.literal("cmd")
			);
			field.setText(backing.get(idx));
			field.setChangedListener(s -> backing.set(idx, s));
		}

		@Override
		public void render(DrawContext context, int ix, int y, int x, int w, int h, int mx, int my, boolean hh, float pt) {
			field.setX(x);
			field.setY(y);
			field.render(context, mx, my, pt);
		}
		@Override public List<? extends net.minecraft.client.gui.Element> children() {
			return List.of(field);
		}

		@Override public List<? extends Selectable> selectableChildren() { return List.of(field); }
	}
}
