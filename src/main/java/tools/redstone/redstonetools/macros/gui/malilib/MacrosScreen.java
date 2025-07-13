package tools.redstone.redstonetools.macros.gui.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.gui.GuiModConfigs;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.gui.GuiStringListEdit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import tools.redstone.redstonetools.macros.Macro;

import java.util.List;

public class MacrosScreen extends Screen {
	private final Screen parent;
	private final List<Macro> macros;

	public MacrosScreen(Screen parent, List<Macro> macros) {
		super(Text.literal("Macros"));
		this.parent = parent;
		this.macros = macros;
	}

	@Override
	protected void init() {
		super.init();
		this.clearChildren();

		int y = 20;
		for (int i = 0; i < macros.size(); i++) {
			final int idx = i;
			this.addDrawableChild(
					ButtonWidget.builder(Text.literal(macros.get(idx).name), btn -> openMacroEditor(macros.get(idx)))
							.dimensions(10, y, 120, 20)
							.build()
			);
			this.addDrawableChild(
					ButtonWidget.builder(Text.literal("–"), btn -> {
								macros.remove(idx);
								this.init();
							})
							.dimensions(140, y, 20, 20)
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
				ButtonWidget.builder(Text.literal("Done"), btn -> MinecraftClient.getInstance().setScreen(parent))
						.dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
						.build()
		);
	}

	private void openMacroEditor(Macro macross) {
		ConfigStringList config = new ConfigStringList(macross.name, ImmutableList.<String>builder().build());
		config.setStrings(macross.toStringList());

		GuiModConfigs configGui = new GuiModConfigs("redstonetools", null, false, "configa");

		MinecraftClient.getInstance().setScreen(
				new GuiStringListEdit(
						config, configGui, null, this
				)
		);
	}

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
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