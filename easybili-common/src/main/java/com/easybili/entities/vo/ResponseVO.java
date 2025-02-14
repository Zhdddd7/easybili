package com.easybili.entities.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseVO<T> {
    private Integer code;
    private String message;
    private T data;
    private long total; // the number of requests

    public ResponseVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * success response - without data
     */
    public static <T> ResponseVO<T> getSuccessResponseVO() {
        return new ResponseVO<>(200, "Success", null);
    }

    /**
     * success response - with data
     */
    public static <T> ResponseVO<T> getSuccessResponseVO(T data) {
        return new ResponseVO<>(200, "Success", data);
    }

    /**
     * success response - with paginated data
     */
    public static <T> ResponseVO<T> getSuccessResponseVO(T data, long total) {
        ResponseVO<T> response = new ResponseVO<>(200, "Success", data);
        response.setTotal(total);
        return response;
    }

    /**
     * error response - without data
     */
    public static <T> ResponseVO<T> getErrorResponseVO() {
        return new ResponseVO<>(500, "Internal Server Error", null);
    }

    /**
     * error response - with custom message
     */
    public static <T> ResponseVO<T> getErrorResponseVO(String message) {
        return new ResponseVO<>(500, message, null);
    }

    /**
     * error response - with custom message and code
     */
    public static <T> ResponseVO<T> getErrorResponseVO(int code, String message) {
        return new ResponseVO<>(code, message, null);
    }
}
