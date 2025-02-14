package com.easybili.entities.enums;

public enum ResponseCodeEnum {
    // Success
    SUCCESS(200, "Success"),

    // Client Errors
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),

    // Server Errors
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    // Custom Business Codes
    BUSINESS_ERROR(1000, "Business Error"),
    VALIDATION_FAILED(1001, "Validation Failed"),
    DATA_NOT_FOUND(1002, "Data Not Found"),

    // System Errors
    SYSTEM_ERROR(2000, "System Error"),
    DATABASE_ERROR(2001, "Database Error"),
    IO_ERROR(2002, "IO Error"),

    // Authentication and Authorization
    TOKEN_EXPIRED(3000, "Token Expired"),
    TOKEN_INVALID(3001, "Token Invalid"),
    ACCESS_DENIED(3002, "Access Denied"),

    // customized code
    BUSINESS_CUSTOM_ERROR(600, "Custom business error"),
    SERVICE_DEGRADED(610, "Service has been degraded"),
    RATE_LIMIT_EXCEEDED(620, "Rate limit exceeded"),
    NOT_LOGIN(901, "Not login or time out");


    private final int code;
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Get ResponseCodeEnum by code.
     *
     * @param code Response code
     * @return ResponseCodeEnum or null if not found
     */
    public static ResponseCodeEnum fromCode(int code) {
        for (ResponseCodeEnum responseCode : values()) {
            if (responseCode.getCode() == code) {
                return responseCode;
            }
        }
        return null;
    }
}
