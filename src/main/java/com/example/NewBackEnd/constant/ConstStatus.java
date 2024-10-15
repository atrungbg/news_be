package com.example.NewBackEnd.constant;

public class ConstStatus {
    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String INACTIVE_STATUS = "INACTIVE";
    public static final String PENDING = "PENDING";
    public static final  String COMPLETED_STATUS = "COMPLETED";

    public static class OrderStatus {
        public static final String ORDER_STATUS_PENDING = "PENDING";
        public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
        public static final String ORDER_STATUS_PAID = "PAID";
        public static final String ORDER_STATUS_SHIPPING = "SHIPPING";
        public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
        public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    }

    public static class PayOsStatus {
        public static final String PAYOS_STATUS_PENDING = "PENDING";
        public static final String PAYOS_STATUS_CANCELLED = "CANCELLED";
        public static final String PAYOS_STATUS_PAID = "PAID";
    }
}
