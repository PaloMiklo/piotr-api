package com.api.piotr.constant;

public interface ApiPaths {
    public static final String BASE_PATH = "/api";
    public static final String ORDER_PATH = BASE_PATH + "/order";
    public static final String CART = BASE_PATH + "/cart";
    public static final String PRODUCT_PATH = BASE_PATH + "/product";
    public static final String PAID_OPTION_ITEM_PATH = BASE_PATH + "/paid-option-item";

    public static final String LIST = "/list";
    public static final String BASE = "";
    public static final String DETAIL = "/{id}";
    public static final String IMAGE = DETAIL + "/image";
    public static final String RECALCULATE = "/recalculate";

    public static String ORDER_LIST = LIST;
    public static String ORDER_DETAIL = DETAIL;
    public static String ORDER_CREATE = BASE;

    public static String PRODUCT_LIST = LIST;
    public static String PRODUCT_DETAIL = DETAIL;
    public static String PRODUCT_IMAGE = IMAGE;
    public static String PRODUCT_CREATE = BASE;

    public static String CART_RECALCULATE = RECALCULATE;
}
