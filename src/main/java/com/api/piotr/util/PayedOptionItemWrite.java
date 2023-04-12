package com.api.piotr.util;

import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;

public class PayedOptionItemWrite {
    public static PayedOptionItem createInstance(String payedOptionItemCode) {
        return new PayedOptionItem(payedOptionItemCode,
                payedOptionItemCode,
                extractCodeAfterDash(payedOptionItemCode),
                null);
    }

    private static PayedOption extractCodeAfterDash(String input) {
        int dashIndex = input.indexOf("-");
        if (dashIndex != -1 && dashIndex + 1 < input.length()) {
            return new PayedOption(input.substring(dashIndex + 1));
        } else {
            throw new IllegalArgumentException("Input string does not contain the '-' character");
        }
    }
}
