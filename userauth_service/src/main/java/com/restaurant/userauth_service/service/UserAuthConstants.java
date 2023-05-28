package com.restaurant.userauth_service.service;

public final class UserAuthConstants {
    public static final String SECRET = "gKYKxIeRfM8DUKpO2B4KbqLaflzGAhbfG/pK/ThoDbXi9BHrQpRTrUue2VaTS2OLVJQBhwIw30vMviloAh4Ebw==";
    public static final String USERNAME_IS_INVALID =
            "The username can only contain english letters, " +
                    "numbers, underscores, spaces, and must be no longer than 10 characters";

    public static final String USERNAME_IS_TAKEN = "This username is already taken";
    public static final String EMAIL_IS_TAKEN = "This email is already taken";
    public static final String EMAIL_IS_INVALID = "This email is invalid";
    public static final String EMAIL_NOT_FOUND = "User not found. Please check your email";
    public static final String WRONG_PASSWORD = "Your password is wrong";
    public static final String REFRESH_NOT_FOUND = "refresh is not found";
    public static final String REFRESH_EXPIRED = "refresh is expired";

    public static final String INVALID_ACCESS = "invalid access token";
    public static final String MANAGER_MESSAGE = """
            You are allowed to change other user's roles,
            see the list of users,
            see the list of dishes,
            add dishes to dish table,
            change dishes price""";
    public static final String CHEF_MESSAGE = """
            You are allowed to see order list,
            change order status,
            get order info by id.""";
    public static final String CUSTOMER_MESSAGE = """
            You are allowed to see menu,
            make orders,
            and get order info by id.""";
}
