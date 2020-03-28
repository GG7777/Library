package net.onlinelibrary.util;

import org.springframework.stereotype.Component;

@Component
public class NumberNormalizer {
    public Integer normalize(Integer number, Integer minValue, Integer maxValue) {
        return number < minValue
                ? minValue
                : number > maxValue
                    ? maxValue
                    : number;
    }
}
