package com.example.NewBackEnd.constant;

public class ConstAPI {
    public static class AuthenticationAPI {
        public static final String LOGIN_WITH_PASSWORD_USERNAME = "api/v1/auth/login";
        public static final String LOGIN_WITH_GOOGLE = "api/v1/auth/login-google";
    }

    public static class UserAPI {

        public static final String CREATE_ACCOUNT = "api/v1/account/create";
        public static final String GET_ACCOUNT_BY_ID = "api/v1/account/";
        public static final String GET_ALL_ACCOUNT = "api/v1/account";
        public static  final String GET_ALL_ACCOUNT_ACTIVE = "api/v1/account-active";
        public static  final String UPDATE_USER = "api/v1/account";

    }

    public static class CacheAPI{
        public static final String CLEAR_CACHE = "api/v1/cache/clear/";
    }

    public static class ProductAPI {
        public static final String CREATE_PRODUCT = "api/v1/product/create";
        public static final String UPDATE_PRODUCT = "api/v1/product/update";
        public static final String DELETE_PRODUCT = "api/v1/product/delete/";
        public static final String GET_PRODUCT_BY_ID = "api/v1/product/";
        public static final String GET_ALL_PRODUCT = "api/v1/product";
        public static final String GET_ALL_PRODUCT_STATUS_TRUE = "api/v1/product/product-status-active";
        public static final String GET_PRODUCT_BY_NAME = "api/v1/product/product-name";
        public static final String GET_PRODUCT_BY_PRICE_RANGE = "api/v1/product/product-price-range";
    }

    public static class CategoryAPI {

        public static final String  CREATE_CATEGORY = "api/v1/category/create";
        public static final String  UPDATE_CATEGORY = "api/v1/category/update";
        public static final String  DELETE_CATEGORY = "api/v1/category/delete/";
        public static final String  GET_CATEGORY_BY_ID = "api/v1/category/";
        public static final String  GET_ALL_CATEGORY = "api/v1/category";
        public static final String  GET_ALL_CATEGORY_STATUS_TRUE = "api/v1/category/category-status-active";

    }

    public static class BaseModelAPI {

        public static final String CREATE_BASE_MODEL = "api/v1/base-model/create";
        public static final String UPDATE_BASE_MODEL = "api/v1/base-model/update";
        public static final String DELETE_BASE_MODEL = "api/v1/base-model/delete/";
        public static final String GET_BASE_MODEL_BY_ID = "api/v1/base-model/";
        public static final String GET_ALL_BASE_MODELS = "api/v1/base-model";
        public static final String GET_ALL_BASE_MODELS_BY_CATEGORY = "api/v1/base-model/category/";
        public static final String GET_ALL_BASE_MODELS_STATUS_ACTIVE = "api/v1/base-model/base-model-status-active";

    }

    public static class ReviewAPI {
        public static final String CREATE_REVIEW = "api/v1/review/create";
        public static final String UPDATE_REVIEW = "api/v1/review/update";
        public static final String DELETE_REVIEW = "api/v1/review/delete/";
        public static final String GET_REVIEW_BY_ID = "api/v1/review/";
        public static final String GET_ALL_REVIEWS = "api/v1/review";
        public static final String GET_REVIEWS_BY_PRODUCT_ID = "api/v1/review/product/";
        public static final String GET_ALL_REVIEWS_STATUS_ACTIVE = "api/v1/review/review-status-active";
    }

    public static class CustomizationOptionAPI {
        public static final String CREATE_CUSTOMIZATION_OPTION = "api/v1/customization-option/create";
        public static final String UPDATE_CUSTOMIZATION_OPTION = "api/v1/customization-option/update";
        public static final String DELETE_CUSTOMIZATION_OPTION = "api/v1/customization-option/delete/";
        public static final String GET_CUSTOMIZATION_OPTION_BY_ID = "api/v1/customization-option/";
        public static final String GET_ALL_CUSTOMIZATION_OPTION = "api/v1/customization-option";
        public static final String GET_ALL_CUSTOMIZATION_OPTION_STATUS_TRUE = "api/v1/customization-option/customization-option-status-active";
    }

    public static class OderItemAPI {
        public static final String CREATE_ORDER_ITEM = "api/v1/order-item/create";
        public static final String UPDATE_ORDER_ITEM = "api/v1/order-item/update";
        public static final String DELETE_ORDER_ITEM = "api/v1/order-item/delete/";
        public static final String GET_ORDER_ITEM_BY_ID = "api/v1/order-item/";
        public static final String GET_ALL_ORDER_ITEMS = "api/v1/order-item";
        public static final String GET_ALL_ORDER_ITEMS_STATUS_TRUE = "api/v1/order-item/order-item-status-active";
    }

    public static class OrderAPI {
        public static final String CREATE_ORDER = "api/v1/order/create";
        public static final String UPDATE_ORDER = "api/v1/order/update";
        public static final String CHANGE_ORDER_STATUS = "api/v1/order/change-status/";
        public static final String GET_ORDER_BY_ID = "api/v1/order/";
        public static final String GET_ALL_ORDERS = "api/v1/order";
        public static final String GET_ALL_ACTIVE_ORDERS = "api/v1/order/order-status-active";
        public static final String GET_ORDERS_BY_USER_ID = "api/v1/order/user/";
        public static final String GET_ORDER_BY_ORDER_CODE = "api/v1/order/order-code/";
        public static  final String GET_PIE_CHART_DATA = "api/v1/order/get-pie-chart";
        public static final  String GET_TOTAL_AMOUNT_AND_COUNT_BY_STATUS_AND_DATE_RANGE = "api/v1/order/line-chart";
    }

    public static class PAYosAPI {
        //checkout
        public static final String CHECKOUT = "api/v1/payment/create-payment-link";

        //order
        public static final String CREATE_ORDER = "api/v1/payment/create";
        public static final String GET_ORDER_BY_ID = "api/v1/payment/";
        public static final String CANCEL_ORDER = "api/v1/payment/cancel/";
        public static final String CONFIRM_ORDER = "api/v1/payment/confirm-webhook";

        //payment
        public static final String PAYOS_TRANSFER_HANDLER = "api/v1/payment/payos_transfer_handler";

        //handle payment status
        public static final String HANDLE_PAYMENT_STATUS = "api/v1/payment/handle-payment-status/";
    }

    public static class ProductCustomizationAPI {
        public static final String CREATE_PRODUCT_CUSTOMIZATION = "api/v1/product-customization/create";
        public static final String UPDATE_PRODUCT_CUSTOMIZATION = "api/v1/product-customization/update";
        public static final String DELETE_PRODUCT_CUSTOMIZATION = "api/v1/product-customization/delete/";
        public static final String GET_PRODUCT_CUSTOMIZATION_BY_ID = "api/v1/product-customization/";
        public static final String GET_ALL_PRODUCT_CUSTOMIZATIONS = "api/v1/product-customization";
        public static final String GET_ACTIVE_PRODUCT_CUSTOMIZATIONS = "api/v1/product-customization/product-customization-status-active";

    }

}
