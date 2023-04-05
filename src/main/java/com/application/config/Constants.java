package com.application.config;

public class Constants {
    public static final String ERROR_ENTITY_NOT_FOUND = "SERVICE-1000";
    public static final String INSUFFICIENT_FUNDS = "SERVICE-1001";
    public static final String SERVICE_ERROR = "SERVICE-1002";

    public static final String INTERNAL_SERVER = "INTERNAL_SERVER_ERROR";
    public static final String INVALID_INPUT = "Invalid Input";
    public static final String ROLE_NOT_FOUND = "Role Not found.";
    public static final String ROLE_NOT_ACTIVE = "Role Not Active";
    public static final String REGISTER_USER_METHOD = "RegisterUser()";
    public static final String PLACE_ORDER_METHOD = "PlaceOrder Service";

    public enum ERole {
        ROLE_USER,
        ROLE_MODERATOR,
        ROLE_ADMIN
    }

}
