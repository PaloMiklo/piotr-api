package com.api.piotr.util;

import static com.api.piotr.dsl.CodeReference.createCodeRef;

import com.api.piotr.entity.PaidOption;
import com.api.piotr.entity.PaidOptionItem;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaidOptionItemWrite {
    public static PaidOptionItem createInstance(String paidOptionItemCode) {
        return new PaidOptionItem(paidOptionItemCode,
                paidOptionItemCode,
                extractCodeAfterDash(paidOptionItemCode),
                null);
    }

    private static PaidOption extractCodeAfterDash(String itemCode) {
        var dashIndex = itemCode.indexOf("-");
        if (dashIndex != -1 && dashIndex + 1 < itemCode.length()) {
            return createCodeRef(PaidOption.class, itemCode.substring(dashIndex + 1));
        } else {
            throw new IllegalArgumentException("Input string does not contain the '-' character");
        }
    }
}
