package pers.cxd.springdemo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.cxd.springdemo.exception.http.HttpException;
import pers.cxd.springdemo.exception.http.HttpExceptionImpl;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class HttpExceptionThrower {

    @ExceptionHandler(value = HttpExceptionImpl.class)
    @ResponseBody
    public String throwClientError(HttpServletResponse resp, HttpException e) {
        resp.setStatus(e.code());
        return e.msg();
    }

}
