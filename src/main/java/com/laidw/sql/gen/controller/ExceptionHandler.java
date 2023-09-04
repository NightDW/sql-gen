package com.laidw.sql.gen.controller;

import com.laidw.sql.gen.exception.SqlGenException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * Description of class {@link ExceptionHandler}.
 *
 * @author NightDW 2023/9/3 23:44
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public String handle(SqlGenException sqlGenException) {
        return "系统错误：" + sqlGenException.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public String handle(BindException bindException) {
        return "参数错误：" + bindException.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(",", "[", "]"));
    }
}
