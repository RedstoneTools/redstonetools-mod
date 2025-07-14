package tools.redstone.redstonetools.macros.gui.malilib;

import fi.dy.masa.malilib.gui.GuiStringListEdit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.Macro;

import java.util.ArrayList;
import java.util.List;

public class MacrosScreen extends Screen {
	private final Screen parent;
	private final List<Macro> macros;
	private List<EditBoxWidget> editBoxWidgets = new ArrayList<>();

	public MacrosScreen(Screen parent, List<Macro> macros) {
		super(Text.literal("Macros"));
		this.parent = parent;
		this.macros = macros;
	}

	@Override
	protected void init() {
		this.editBoxWidgets = new ArrayList<>();
		super.init();
		this.clearChildren();

		int y = 20;
		for (int i = 0; i < macros.size(); i++) {
			final int idx = i;
			this.editBoxWidgets.add(new EditBoxWidget(this.textRenderer, 140, y, 120, 20, Text.literal("."), Text.literal(macros.get(idx).name)));
			this.addDrawableChild(
					ButtonWidget.builder(Text.literal(macros.get(idx).name), btn -> openMacroEditor(macros.get(idx)))
							.dimensions(10, y, 120, 20)
							.build()
			);
			this.addDrawableChild(editBoxWidgets.get(idx)
			);
			this.addDrawableChild(
					ButtonWidget.builder(Text.literal("–"), btn -> {
								macros.remove(idx);
								this.init();
							})
							.dimensions(270, y, 20, 20)
							.build()
			);
			y += 24;
		}

		this.addDrawableChild(
				ButtonWidget.builder(Text.literal("+ New Macro"), btn -> {
							macros.add(Macro.buildEmpty());
							this.init();
						})
						.dimensions(10, y, 120, 20)
						.build()
		);

		this.addDrawableChild(
				ButtonWidget.builder(Text.literal("Done"), btn -> {
							for (Macro macro: macros) {
								if (macro.name.isEmpty()) {
									// i wanna show a notification idk how though
									return;
								}
							}
							MinecraftClient.getInstance().setScreen(parent);
						})
						.dimensions(10, this.height - 30, 100, 20)
						.build()
		);
	}
	private void openMacroEditor(Macro macro) {
		MinecraftClient.getInstance().setScreen(
				new GuiStringListEdit(
						macro.config, macro.configGui, null, this
				)
		);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		int idx = 0;
		for (Macro macro : macros) {
			if (!macro.name.equals(editBoxWidgets.get(idx).getText())) {
				if (editBoxWidgets.get(idx).getText().isEmpty()) {
//					editBoxWidgets.get(idx).setText(macro.name);
					// TODO: something is horribly wrong. its hard to explain
					continue;
				}
				String stripped = editBoxWidgets.get(idx).getText();
				stripped.replace("\n", "");
				macro.name = stripped;
				this.init();
			}
			idx++;
		}
		this.renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		drawContext.drawText(
				this.textRenderer,
				this.title,
				this.width / 2,
				6,
				2147483647,
				false
		);
	}
}