package net.onlinelibrary.util;

public class NumberNormalizer {
    public static Integer normalize(Integer number, Integer minValue, Integer maxValue) {
        return number < minValue
                ? minValue
                : number > maxValue
                    ? maxValue
                    : number;
    }
}
