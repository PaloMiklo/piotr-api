package com.api.piotr.util;

import static com.api.piotr.dsl.CodeReference.createCodeRef;

import com.api.piotr.entity.PayedOption;
import com.api.piotr.entity.PayedOptionItem;

import lombok.experimental.UtilityClass;

@UtilityClass
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
            return createCodeRef(PayedOption.class, itemCode.substring(dashIndex + 1));
        } else {
            throw new IllegalArgumentException("Input string does not contain the '-' character");
        }
    }
}
