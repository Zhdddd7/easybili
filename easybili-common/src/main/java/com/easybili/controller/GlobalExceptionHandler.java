package com.easybili.web.controller;

import com.easybili.web.exception.BusinessException;
import com.easybili.web.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger((GlobalExceptionHandler.class));

    @ExceptionHandler(value =  Exception.class)
    Object handelException(Exception e, HttpServletRequest request){
        logger.error("request error, the address{}, the error message:", request.getRequestURI(), e);
        ResponseVO<String> response = new ResponseVO<>();
        if (e instanceof NoHandlerFoundException){
           response.setCode(404);
           response.setMessage("404 not found");
        }
        else if (e instanceof BusinessException){
            BusinessException b = (BusinessException) e;
            response.setCode(b.getCode() == null?600: b.getCode());
            response.setMessage("status error");
        }
        else{
            response.setCode(500);
            response.setMessage("an error happen");
        }
        return response;
    }
}
