package com.example.NewBackEnd.constant;

public class ConstError {
    public static class Input{
        public static final String NO_INPUT = "No input";
    }
    public static class User {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USERNAME_EXISTED = "Username existed";
        public static final String EMAIL_EXISTED = "Email existed";
        public static final String INVALID_AUTHORIZATION_HEADER = "Invalid authorization header";
        public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    }

    public static class Role {
        public static final String ROLE_NOT_FOUND = "Role not found";
    }

    public static class Category {
        public static final String CATEGORY_NOT_FOUND = "Category not found";

    }
    public static class Product {
        public static final String PRODUCT_NOT_FOUND = "Product not found";
        public static final String PRODUCT_STOCK_NOT_ENOUGH = "Product stock not enough";
    }

    public static class BaseModel {
        public static final String BASE_MODEL_NOT_FOUND = "Base model not found";
    }

    public static class Review {
        public static final String REVIEW_NOT_FOUND = "Review not found";
    }

    public static class Order {
        public static final String ORDER_NOT_FOUND = "Order not found";
        public static final String NO_COMPLETED_ORDERS = "No completed orders";
    }

    public static class CustomizationOption {
        public static final String CUSTOMIZATION_OPTION_NOT_FOUND = "Customization option not found";

    }

    public static class OrderItem {
        public static final String ORDER_ITEM_NOT_FOUND = "Order item not found";
    }

    public static class PayOS {
        public static final String INVALID_WEBHOOK_DATA = "Invalid webhook data";
        public static final String WEBHOOK_PROCESS_FAILED = "Webhook process failed";
        public static final String CANCELED_ORDER_FAILED = "Cancel order failed";
        public static final String HANDLE_PAYMENT_FAILED = "Handle payment failed";
    }

    public static class ProductCustomization {
        public static final String PRODUCT_CUSTOMIZATION_NOT_FOUND = "Product customization not found";
    }

}
