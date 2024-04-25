package com.example.clear_solutions_task.utils;

public class ValidationConstants {
    private ValidationConstants(){}
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String INVALID_EMAIL_MESSAGE = """
            Invalid email pattern.Email must contains
            1. Symbol @
            2. Two symbols before @
            3. Should contains .domain
            Example: vs@gmail.com""";

}
