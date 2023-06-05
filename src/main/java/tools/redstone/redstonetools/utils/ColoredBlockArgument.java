package tools.redstone.redstonetools.utils;

public enum ColoredBlockArgument {
    WOOL,
    GLASS,
    CONCRETE,
    TERRACOTTA,
    GLAZED_TERRACOTTA;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
