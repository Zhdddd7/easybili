package com.easybili.web.controller;

import com.easybili.web.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestResponseController extends BaseController<String> {

    /**
     * 测试成功响应 - 简单信息
     */
    @GetMapping("/success")
    public ResponseVO<String> testSuccess() {
        return success("This is a success response");
    }

    /**
     * 测试成功响应 - 分页数据
     */
    @GetMapping("/successWithPagination")
    public ResponseVO<List<String>> testSuccessWithPagination() {
        List<String> data = Arrays.asList("Item1", "Item2", "Item3");
        return success(data, data.size());
    }

    /**
     * 测试错误响应
     */
    @GetMapping("/error")
    public ResponseVO<String> testError() {
        return error("This is an error response");
    }

    /**
     * 测试异常处理
     */
    @GetMapping("/exception")
    public ResponseVO<String> testException() {
        throw new RuntimeException("Test exception occurred");
    }
}
