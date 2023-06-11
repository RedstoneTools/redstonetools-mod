package tools.redstone.redstonetools.utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextUtils {

    private static class SharedInt {
        public int value;
    }

    /** Joins all given text components without a seperator. */
    public static MutableText join(MutableText... texts) {
        MutableText text = new LiteralText("");
        for (Text tx : texts) {
            text.append(tx);
        }

        return text;
    }

    /** Replace all literal placeholders in the given text with the provided values. */
    public static MutableText format(Text text, Object... values) {
        return format0(text, new SharedInt(), values);
    }

    private static String formatStringShared(String str, SharedInt paramIndex, Object... values) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\' && i != str.length() - 1 && str.charAt(i + 1) == '{') {
                // escaped placeholder, dont handle
                i++;
                b.append('{');
            } else if (c == '{') {
                // format placeholder
                i++;

                boolean formatPlaceholder = true;
                if (str.charAt(i) == '~') {
                    formatPlaceholder = false;
                    i++;
                }

                if (str.charAt(i) == '}') {
                    if (formatPlaceholder)
                        b.append(Formatting.RED);

                    // no specific index provided, use
                    // the shared paramIndex
                    b.append(values[paramIndex.value++]);

                    if (formatPlaceholder)
                        b.append(Formatting.RESET);
                } else {
                    // todo: add ability to specify a specific
                    //  value like {IDX}
                }
            } else {
                // append character
                b.append(c);
            }
        }

        return b.toString();
    }

    private static MutableText format0(Text text, SharedInt paramIndex, Object... values) {
        MutableText result;

        if (text instanceof LiteralText literalText) {
            // format string
            result = new LiteralText(formatStringShared(literalText.getRawString(),
                    paramIndex, values));
        } else /* unsupported type */ {
            return text.copy();
        }

        // format children
        for (Text child : text.getSiblings()) {
            result.append(format0(child, paramIndex, values));
        }

        return result;
    }

}
