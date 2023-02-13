package com.domain.redstonetools.utils;

public class ColorCodeUtils {
    public static final char COLOR_CODE_CHAR = '§';

    public enum Color {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        OBFUSCATED('k'),
        BOLD('l'),
        STRIKETHROUGH('m'),
        UNDERLINE('n'),
        ITALIC('o'),
        RESET('r');

        public final char code;

        Color(char code) {
            this.code = code;
        }
    }

}
