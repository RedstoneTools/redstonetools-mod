package tools.redstone.redstonetools.utils;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class NumberArg implements Comparable<NumberArg>  {
    public final BigInteger numValue;

    public final Integer originalBase;

    public NumberArg(String num, int base) {
        this.numValue = new BigInteger(num, base);
        this.originalBase = base;
    }

    public BigInteger getNum(){
        return this.numValue;
    }
    public Integer getBase(){
        return this.originalBase;
    }

    /**
     * Returns the number in a string format without a base prefix.
     * @return Formatted String
     */
    public String toString(){
        return this.toString(originalBase);
    }

    /**
     * Returns the number in a string format converted to another radix and without a base prefix.
     * @param radix The Radix to turn use on the number
     * @return Formatted String
     */
    public String toString(int radix){
        return this.numValue.toString(radix);
    }

    /**
     * Returns the number in a string format with a base prefix.
     * @return Formatted String
     */
    public String toPrefixedString(){
        return toPrefixedString(this.originalBase);
    }

    /**
     * Returns the number in a string format converted to another radix and with a base prefix.
     * @param radix The Radix to turn use on the number
     * @return Formatted String
     */
    public String toPrefixedString(int radix){
        return NumberBase.formatNumber(this.numValue.toString(radix),radix);
    }

    @Override
    public int compareTo(@NotNull NumberArg o) {
        return this.numValue.compareTo(o.numValue);
    }
}
