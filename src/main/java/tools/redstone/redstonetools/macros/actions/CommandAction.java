package tools.redstone.redstonetools.macros.actions;

import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.LiteralText;
import tools.redstone.redstonetools.RedstoneToolsClient;

import java.util.Objects;

public class CommandAction extends Action {
    private final String command;
    private Script script;

    public CommandAction(String command) {
        this.command = command;

        Parser.Result result = Parser.parse(command);
        if (result.hasErrors()) {
            MinecraftClient.getInstance().getToastManager().add(
                    new SystemToast(
                            SystemToast.Type.PERIODIC_NOTIFICATION,
                            new LiteralText("Macro Error"),
                            new LiteralText(result.errors.get(0).toString())
                    )
            );
            return;
        }
        this.script = Compiler.compile(result);
    }

    @Override
    public void run() {
        var player = MinecraftClient.getInstance().player;
        assert player != null;

        String toSend = command;
        if (this.script != null) {
            toSend = RedstoneToolsClient.STARSCRIPT.run(this.script).toString();
        }
        player.sendChatMessage(toSend.startsWith("/") ? toSend : "/" + toSend);
    }

    public String getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandAction that)) return false;
        return Objects.equals(command, that.command);
    }
}
