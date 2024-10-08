package com.toiec.toiec.dto.enums;

public enum StatusCodeEnum {
    //EXCEPTION
    EXCEPTION("EXCEPTION"), // Exception
    EXCEPTION0400("EXCEPTION0400"), // Bad request
    EXCEPTION0404("EXCEPTION0404"), // Not found
    EXCEPTION0503("EXCEPTION0503"), // Http message not readable
    EXCEPTION0504("EXCEPTION0504"), // Missing servlet request parameter
    EXCEPTION0505("EXCEPTION0505"), // Access Denied/Not have permission

    //USER
    USERTEST0000("USER0000"), // Create user test success
    USERTEST1000("USER1000"); // Create user test failed

    public final String value;

    StatusCodeEnum(String i) {
        value = i;
    }
}
