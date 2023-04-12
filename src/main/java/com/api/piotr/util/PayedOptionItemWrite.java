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

    private static PayedOption extractCodeAfterDash(String itemCode) {
        int dashIndex = itemCode.indexOf("-");
        if (dashIndex != -1 && dashIndex + 1 < itemCode.length()) {
            return new PayedOption(itemCode.substring(dashIndex + 1));
        } else {
            throw new IllegalArgumentException("Input string does not contain the '-' character");
        }
    }
}
