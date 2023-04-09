package com.domain.redstonetools.macros.gui.commandsuggestor;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class WorldlessCommandSuggestor {

    public static final ClientPlayNetworkHandler dummyNetworkHandler;
    private static final WorldlessCommandSource commandSource;
    public static boolean registered = false;


    static {
        dummyNetworkHandler = new ClientPlayNetworkHandler(MinecraftClient.getInstance(), null,new ClientConnection(NetworkSide.CLIENTBOUND) , new GameProfile(null, "Player0"), null);
        commandSource = new WorldlessCommandSource();

        new CommandManager(CommandManager.RegistrationEnvironment.DEDICATED); //registers commands using CommandDispatcherMixin
    }


    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
    private static final Style ERROR_STYLE = Style.EMPTY.withColor(Formatting.RED);
    private static final Style INFO_STYLE = Style.EMPTY.withColor(Formatting.GRAY);
    private static final List<Style> HIGHLIGHT_STYLES;

    static {
        Stream<Formatting> var10000 = Stream.of(Formatting.AQUA, Formatting.YELLOW, Formatting.GREEN, Formatting.LIGHT_PURPLE, Formatting.GOLD);
        Style var10001 = Style.EMPTY;
        Objects.requireNonNull(var10001);
        HIGHLIGHT_STYLES = var10000.map(var10001::withColor).collect(ImmutableList.toImmutableList());
    }

    final MinecraftClient client;
    final Screen owner;
    final TextFieldWidget textField;
    final TextRenderer textRenderer;
    private final boolean slashOptional;
    private final boolean suggestingWhenEmpty;
    final int inWindowIndexOffset;
    final int maxSuggestionSize;
    final boolean chatScreenSized;
    final int color;
    private final List<OrderedText> messages = Lists.newArrayList();
    private int x;
    private int width;
    @Nullable
    private ParseResults<CommandSource> parse;
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Nullable
    WorldlessCommandSuggestor.SuggestionWindow window;
    private boolean windowActive;
    boolean completingSuggestions;

    public WorldlessCommandSuggestor(MinecraftClient client, Screen owner, TextFieldWidget textField, TextRenderer textRenderer, boolean slashOptional, boolean suggestingWhenEmpty, int inWindowIndexOffset, int maxSuggestionSize, boolean chatScreenSized, int color) {
        this.client = client;
        this.owner = owner;
        this.textField = textField;
        this.textRenderer = textRenderer;
        this.slashOptional = slashOptional;
        this.suggestingWhenEmpty = suggestingWhenEmpty;
        this.inWindowIndexOffset = inWindowIndexOffset;
        this.maxSuggestionSize = maxSuggestionSize;
        this.chatScreenSized = chatScreenSized;
        this.color = color;
        textField.setRenderTextProvider(this::provideRenderText);
    }

    public void setWindowActive(boolean windowActive) {
        this.windowActive = windowActive;
        if (!windowActive) {
            this.window = null;
        }

    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.window != null && this.window.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.owner.getFocused() == this.textField && keyCode == 258) {
            this.showSuggestions(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double amount) {

        return this.window != null && this.window.mouseScrolled(MathHelper.clamp(amount, -1.0, 1.0));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.window != null && this.window.mouseClicked((int) mouseX, (int) mouseY, button);
    }

    public void showSuggestions(boolean narrateFirstSuggestion) {
        if (this.pendingSuggestions != null && this.pendingSuggestions.isDone()) {
            Suggestions suggestions = this.pendingSuggestions.join();
            if (!suggestions.isEmpty()) {
                int i = 0;

                Suggestion suggestion;
                for (Iterator<Suggestion> var4 = suggestions.getList().iterator(); var4.hasNext(); i = Math.max(i, this.textRenderer.getWidth(suggestion.getText()))) {
                    suggestion = var4.next();
                }

                int j = MathHelper.clamp(this.textField.getCharacterX(suggestions.getRange().getStart()), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
                int k = this.chatScreenSized ? this.owner.height - 12 : 72;
                this.window = new WorldlessCommandSuggestor.SuggestionWindow(j, k, i, this.sortSuggestions(suggestions), narrateFirstSuggestion);
            }
        }

    }

    private List<Suggestion> sortSuggestions(Suggestions suggestions) {
        String string = this.textField.getText().substring(0, this.textField.getCursor());
        int i = getStartOfCurrentWord(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        List<Suggestion> list = Lists.newArrayList();
        List<Suggestion> list2 = Lists.newArrayList();


        for (Suggestion suggestion : suggestions.getList()) {
            if (!suggestion.getText().startsWith(string2) && !suggestion.getText().startsWith("minecraft:" + string2)) {
                list2.add(suggestion);
            } else {
                list.add(suggestion);
            }
        }

        list.addAll(list2);
        return list;

    }

    public void refresh(boolean showSuggestion) {
        String string = this.textField.getText();
        if (this.parse != null && !this.parse.getReader().getString().equals(string)) {
            this.parse = null;
        }

        if (!this.completingSuggestions) {
            this.textField.setSuggestion(null);
            this.window = null;
        }

        this.messages.clear();
        StringReader stringReader = new StringReader(string);
        boolean bl = stringReader.canRead() && stringReader.peek() == '/';
        if (bl) {
            stringReader.skip();
        }

        boolean bl2 = this.slashOptional || bl;
        int i = this.textField.getCursor();
        int j;
        if (bl2) {

            CommandDispatcher<CommandSource> commandDispatcher = dummyNetworkHandler.getCommandDispatcher();
            if (this.parse == null) {
                this.parse = commandDispatcher.parse(stringReader, commandSource);
            }

            j = this.suggestingWhenEmpty ? stringReader.getCursor() : 1;
            if (i >= j && (this.window == null || !this.completingSuggestions)) {
                this.pendingSuggestions = commandDispatcher.getCompletionSuggestions(this.parse, i);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.show(showSuggestion);
                    }
                });
            }
        }

    }

    private static int getStartOfCurrentWord(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return 0;
        } else {
            int i = 0;

            Matcher matcher = WHITESPACE_PATTERN.matcher(input);
            while (matcher.find()) {
                i = matcher.end();
            }

            return i;
        }
    }

    private static OrderedText formatException(CommandSyntaxException exception) {
        Text text = Texts.toText(exception.getRawMessage());
        String string = exception.getContext();
        return string == null ? text.asOrderedText() : (new TranslatableText("command.context.parse_error", text, exception.getCursor(), string)).asOrderedText();
    }

    private void show(boolean showSuggestion) {
        if (this.textField.getCursor() == this.textField.getText().length()) {
            if ((this.pendingSuggestions.join()).isEmpty() && !this.parse.getExceptions().isEmpty()) {
                int i = 0;

                for (Map.Entry<CommandNode<CommandSource>, CommandSyntaxException> entry : this.parse.getExceptions().entrySet()) {
                    CommandSyntaxException commandSyntaxException = entry.getValue();
                    if (commandSyntaxException.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
                        ++i;
                    } else {
                        this.messages.add(formatException(commandSyntaxException));
                    }
                }

                if (i > 0) {
                    this.messages.add(formatException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create()));
                }
            } else if (this.parse.getReader().canRead()) {
                this.messages.add(formatException(CommandManager.getException(this.parse)));
            }
        }

        this.x = 0;
        this.width = this.owner.width;
        if (this.messages.isEmpty() && showSuggestion) {
            this.showUsages();
        }

        this.window = null;
        if (this.windowActive && this.client.options.autoSuggestions) {
            this.showSuggestions(false);
        }

    }

    private void showUsages() {
        CommandContextBuilder<CommandSource> commandContextBuilder = this.parse.getContext();

        SuggestionContext<CommandSource> suggestionContext = commandContextBuilder.findSuggestionContext(this.textField.getCursor());

        Map<CommandNode<CommandSource>, String> map = dummyNetworkHandler.getCommandDispatcher().getSmartUsage(suggestionContext.parent, commandSource);
        List<OrderedText> list = Lists.newArrayList();
        int i = 0;
        Style style = Style.EMPTY.withColor(Formatting.GRAY);

        for (Map.Entry<CommandNode<CommandSource>, String> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof LiteralCommandNode)) {
                list.add(OrderedText.styledForwardsVisitedString(entry.getValue(), style));
                i = Math.max(i, this.textRenderer.getWidth(entry.getValue()));
            }
        }

        if (!list.isEmpty()) {
            this.messages.addAll(list);
            this.x = MathHelper.clamp(this.textField.getCharacterX(suggestionContext.startPos), 0, this.textField.getCharacterX(0) + this.textField.getInnerWidth() - i);
            this.width = i;
        }

    }

    private OrderedText provideRenderText(String original, int firstCharacterIndex) {
        return this.parse != null ? highlight(this.parse, original, firstCharacterIndex) : OrderedText.styledForwardsVisitedString(original, Style.EMPTY);
    }

    @Nullable
    static String getSuggestionSuffix(String original, String suggestion) {
        return suggestion.startsWith(original) ? suggestion.substring(original.length()) : null;
    }

    private static OrderedText highlight(ParseResults<CommandSource> parse, String original, int firstCharacterIndex) {
        List<OrderedText> list = Lists.newArrayList();
        int i = 0;
        int j = -1;
        CommandContextBuilder<CommandSource> commandContextBuilder = parse.getContext().getLastChild();

        for (ParsedArgument<CommandSource, ?> parsedArgument : commandContextBuilder.getArguments().values()) {
            ++j;
            if (j >= HIGHLIGHT_STYLES.size()) {
                j = 0;
            }

            int k = Math.max(parsedArgument.getRange().getStart() - firstCharacterIndex, 0);
            if (k >= original.length()) {
                break;
            }

            int l = Math.min(parsedArgument.getRange().getEnd() - firstCharacterIndex, original.length());
            if (l > 0) {
                list.add(OrderedText.styledForwardsVisitedString(original.substring(i, k), INFO_STYLE));
                list.add(OrderedText.styledForwardsVisitedString(original.substring(k, l), HIGHLIGHT_STYLES.get(j)));
                i = l;
            }
        }

        if (parse.getReader().canRead()) {
            int m = Math.max(parse.getReader().getCursor() - firstCharacterIndex, 0);
            if (m < original.length()) {
                int n = Math.min(m + parse.getReader().getRemainingLength(), original.length());
                list.add(OrderedText.styledForwardsVisitedString(original.substring(i, m), INFO_STYLE));
                list.add(OrderedText.styledForwardsVisitedString(original.substring(m, n), ERROR_STYLE));
                i = n;
            }
        }

        list.add(OrderedText.styledForwardsVisitedString(original.substring(i), INFO_STYLE));
        return OrderedText.concat(list);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.window != null) {
            this.window.render(matrices, mouseX, mouseY);
        } else {
            int i = 0;

            for (Iterator<OrderedText> var5 = this.messages.iterator(); var5.hasNext(); ++i) {
                OrderedText orderedText = var5.next();
                int j = this.chatScreenSized ? this.owner.height - 14 - 13 - 12 * i : 72 + 12 * i;
                DrawableHelper.fill(matrices, this.x - 1, j, this.x + this.width + 1, j + 12, this.color);
                this.textRenderer.drawWithShadow(matrices, orderedText, (float) this.x, (float) (j + 2), -1);
            }
        }

    }

    public String getNarration() {
        return this.window != null ? "\n" + this.window.getNarration() : "";
    }

    public class SuggestionWindow {
        private final Rect2i area;
        private final String typedText;
        private final List<Suggestion> suggestions;
        private int inWindowIndex;
        private int selection;
        private Vec2f mouse;
        private boolean completed;
        private int lastNarrationIndex;

        SuggestionWindow(int x, int y, int width, List<Suggestion> suggestions, boolean narrateFirstSuggestion) {
            this.mouse = Vec2f.ZERO;
            int i = x - 1;
            int j = WorldlessCommandSuggestor.this.chatScreenSized ? y - 3 - Math.min(suggestions.size(), WorldlessCommandSuggestor.this.maxSuggestionSize) * 12 : y;
            this.area = new Rect2i(i, j, width + 1, Math.min(suggestions.size(), WorldlessCommandSuggestor.this.maxSuggestionSize) * 12);
            this.typedText = WorldlessCommandSuggestor.this.textField.getText();
            this.lastNarrationIndex = narrateFirstSuggestion ? -1 : 0;
            this.suggestions = suggestions;
            this.select(0);
        }

        public void render(MatrixStack matrices, int mouseX, int mouseY) {
            int i = Math.min(this.suggestions.size(), WorldlessCommandSuggestor.this.maxSuggestionSize);

            boolean bl = this.inWindowIndex > 0;
            boolean bl2 = this.suggestions.size() > this.inWindowIndex + i;
            boolean bl3 = bl || bl2;
            boolean bl4 = this.mouse.x != (float) mouseX || this.mouse.y != (float) mouseY;
            if (bl4) {
                this.mouse = new Vec2f((float) mouseX, (float) mouseY);
            }

            if (bl3) {
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() - 1, this.area.getX() + this.area.getWidth(), this.area.getY(), WorldlessCommandSuggestor.this.color);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + this.area.getHeight(), this.area.getX() + this.area.getWidth(), this.area.getY() + this.area.getHeight() + 1, WorldlessCommandSuggestor.this.color);
                int k;
                if (bl) {
                    for (k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 == 0) {
                            DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() - 1, this.area.getX() + k + 1, this.area.getY(), -1);
                        }
                    }
                }

                if (bl2) {
                    for (k = 0; k < this.area.getWidth(); ++k) {
                        if (k % 2 == 0) {
                            DrawableHelper.fill(matrices, this.area.getX() + k, this.area.getY() + this.area.getHeight(), this.area.getX() + k + 1, this.area.getY() + this.area.getHeight() + 1, -1);
                        }
                    }
                }
            }

            boolean bl5 = false;

            for (int l = 0; l < i; ++l) {
                Suggestion suggestion = this.suggestions.get(l + this.inWindowIndex);
                DrawableHelper.fill(matrices, this.area.getX(), this.area.getY() + 12 * l, this.area.getX() + this.area.getWidth(), this.area.getY() + 12 * l + 12, WorldlessCommandSuggestor.this.color);
                if (mouseX > this.area.getX() && mouseX < this.area.getX() + this.area.getWidth() && mouseY > this.area.getY() + 12 * l && mouseY < this.area.getY() + 12 * l + 12) {
                    if (bl4) {
                        this.select(l + this.inWindowIndex);
                    }

                    bl5 = true;
                }

                WorldlessCommandSuggestor.this.textRenderer.drawWithShadow(matrices, suggestion.getText(), (float) (this.area.getX() + 1), (float) (this.area.getY() + 2 + 12 * l), l + this.inWindowIndex == this.selection ? -256 : -5592406);
            }

            if (bl5) {
                Message message = (this.suggestions.get(this.selection)).getTooltip();
                if (message != null) {
                    WorldlessCommandSuggestor.this.owner.renderTooltip(matrices, Texts.toText(message), mouseX, mouseY);
                }
            }

        }

        public boolean mouseClicked(int x, int y, int button) {
            if (!this.area.contains(x, y)) {
                return false;
            } else {
                int i = (y - this.area.getY()) / 12 + this.inWindowIndex;
                if (i >= 0 && i < this.suggestions.size()) {
                    this.select(i);
                    this.complete();
                }

                return true;
            }
        }

        public boolean mouseScrolled(double amount) {
            int i = (int) (WorldlessCommandSuggestor.this.client.mouse.getX() * (double) WorldlessCommandSuggestor.this.client.getWindow().getScaledWidth() / (double) WorldlessCommandSuggestor.this.client.getWindow().getWidth());
            int j = (int) (WorldlessCommandSuggestor.this.client.mouse.getY() * (double) WorldlessCommandSuggestor.this.client.getWindow().getScaledHeight() / (double) WorldlessCommandSuggestor.this.client.getWindow().getHeight());
            if (this.area.contains(i, j)) {
                this.inWindowIndex = MathHelper.clamp((int) ((double) this.inWindowIndex - amount), 0, Math.max(this.suggestions.size() - WorldlessCommandSuggestor.this.maxSuggestionSize, 0));
                return true;
            } else {
                return false;
            }
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (keyCode == 265) {
                this.scroll(-1);
                this.completed = false;
                return true;
            } else if (keyCode == 264) {
                this.scroll(1);
                this.completed = false;
                return true;
            } else if (keyCode == 258) {
                if (this.completed) {
                    this.scroll(Screen.hasShiftDown() ? -1 : 1);
                }

                this.complete();
                return true;
            } else if (keyCode == 256) {
                this.discard();
                return true;
            } else {
                return false;
            }
        }

        public void scroll(int offset) {
            this.select(this.selection + offset);
            int i = this.inWindowIndex;
            int j = this.inWindowIndex + WorldlessCommandSuggestor.this.maxSuggestionSize - 1;
            if (this.selection < i) {
                this.inWindowIndex = MathHelper.clamp(this.selection, 0, Math.max(this.suggestions.size() - WorldlessCommandSuggestor.this.maxSuggestionSize, 0));
            } else if (this.selection > j) {
                this.inWindowIndex = MathHelper.clamp(this.selection + WorldlessCommandSuggestor.this.inWindowIndexOffset - WorldlessCommandSuggestor.this.maxSuggestionSize, 0, Math.max(this.suggestions.size() - WorldlessCommandSuggestor.this.maxSuggestionSize, 0));
            }

        }

        public void select(int index) {
            this.selection = index;
            if (this.selection < 0) {
                this.selection += this.suggestions.size();
            }

            if (this.selection >= this.suggestions.size()) {
                this.selection -= this.suggestions.size();
            }

            Suggestion suggestion = this.suggestions.get(this.selection);
            WorldlessCommandSuggestor.this.textField.setSuggestion(WorldlessCommandSuggestor.getSuggestionSuffix(WorldlessCommandSuggestor.this.textField.getText(), suggestion.apply(this.typedText)));
            if (this.lastNarrationIndex != this.selection) {
                NarratorManager.INSTANCE.narrate(this.getNarration());
            }

        }

        public void complete() {
            Suggestion suggestion = this.suggestions.get(this.selection);
            WorldlessCommandSuggestor.this.completingSuggestions = true;
            WorldlessCommandSuggestor.this.textField.setText(suggestion.apply(this.typedText));
            int i = suggestion.getRange().getStart() + suggestion.getText().length();
            WorldlessCommandSuggestor.this.textField.setSelectionStart(i);
            WorldlessCommandSuggestor.this.textField.setSelectionEnd(i);
            this.select(this.selection);
            WorldlessCommandSuggestor.this.completingSuggestions = false;
            this.completed = true;
        }

        Text getNarration() {
            this.lastNarrationIndex = this.selection;
            Suggestion suggestion = this.suggestions.get(this.selection);
            Message message = suggestion.getTooltip();
            return message != null ? new TranslatableText("narration.suggestion.tooltip", this.selection + 1, this.suggestions.size(), suggestion.getText(), message) : new TranslatableText("narration.suggestion", this.selection + 1, this.suggestions.size(), suggestion.getText());
        }

        public void discard() {
            WorldlessCommandSuggestor.this.window = null;
        }
    }

}
