package tools.redstone.redstonetools.utils;

import java.util.Arrays;
import java.util.Optional;

public enum NumberBase {
    BINARY(2),
    OCTAL(8),
    DECIMAL(10),
    HEXADECIMAL(16);

    private final int base;

    NumberBase(int base) {
        this.base = base;
    }

    public int toInt() {
        return base;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static Optional<NumberBase> fromString(String string) {
        return Arrays.stream(values())
                .filter(b -> b.toString().equalsIgnoreCase(string))
                .findFirst();
    }

    public static Optional<NumberBase> fromInt(int i) {
        return Arrays.stream(values())
                .filter(b -> b.toInt() == i)
                .findFirst();
    }
}
