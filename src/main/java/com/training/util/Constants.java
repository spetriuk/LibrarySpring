package com.training.util;

public class Constants {
    public static final String REGEX_PHONE = "^\\+?3?8?(0\\d{9})$";
    public static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=\\\\S+$).{8,}";
    public static final String CYRILLIC_REGEX = "^[\\p{InCyrillic}\\s]+$";
    public static final String ENGLISH_REGEX = "^[a-zA-Z0-9$@$!%*?&#^-_. +]+$";

    public static final String MESSAGE_ENGLISH = "{message.english}";
    public static final String MESSAGE_CYRILLIC = "{message.cyrillic}";
    public static final String MESSAGE_EMAIL = "{message.email}";
    public static final String MESSAGE_EMPTY = "{message.empty}";
    public static final String MESSAGE_PHONE = "{message.phone}";
    public static final String MESSAGE_PASSWORD = "{message.password}";
}
