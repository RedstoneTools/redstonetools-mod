package tools.redstone.redstonetools.utils;

import java.util.Arrays;
import java.util.Optional;

public enum NumberBase {
    BINARY(2,'b'),
    OCTAL(8,'o'),
    DECIMAL(10,'d'),
    HEXADECIMAL(16,'x');

    private final int base;

    private final char prefixChar;

    NumberBase(int base,char prefixChar) {
        this.base = base;
        this.prefixChar = prefixChar;
    }

    public int toInt() {
        return base;
    }

    public char getPrefixChar() {return prefixChar;}

    public String getPrefix(){
        return "0" + prefixChar;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /**
     * Returns an Optional for a NumberBase. Returns empty Optional if not found.
     * @param string Search input for the NumberBase
     * @return Optional for a NumberBase
     */
    public static Optional<NumberBase> fromString(String string) {
        return Arrays.stream(values())
                .filter(b -> b.toString().equalsIgnoreCase(string))
                .findFirst();
    }

    /**
     * Returns an Optional for a NumberBase. Returns empty Optional if not found.
     * @param i Search input for the NumberBase
     * @return Optional for a NumberBase
     */
    public static Optional<NumberBase> fromInt(int i) {
        return Arrays.stream(values())
                .filter(b -> b.toInt() == i)
                .findFirst();
    }

    /**
     * Returns an Optional for a NumberBase. Returns empty Optional if not found.
     * @param c Search input for the NumberBase
     * @return Optional for a NumberBase
     */
    public static Optional<NumberBase> fromCharacter(char c) {
        return Arrays.stream(values())
                .filter(b -> b.getPrefixChar() == c)
                .findFirst();
    }

    /**
     * Returns an Optional for a NumberBase. Returns empty Optional if not found.
     * @param prefix Search input for the NumberBase
     * @return Optional for a NumberBase
     */
    public static Optional<NumberBase> fromPrefix(String prefix) {
        return Arrays.stream(values())
                .filter(b -> b.getPrefix().equalsIgnoreCase(prefix))
                .findFirst();
    }

    /**
     * Returns a formatted String that includes the base and the number prefix together
     * @param num Number to be formatted:
     * @param base Base to be paired with the number
     * @return Formatted String
     */
    public static String formatNumber(String num, int base) {
        num = num.toUpperCase();
        return switch (base) {
            case 2 -> "0b" + num;
            case 8 -> "0o" + num;
            case 10 -> "0d" + num;
            case 16 -> "0x" + num;
            default -> num + "_" + base;
        };
    }
}
