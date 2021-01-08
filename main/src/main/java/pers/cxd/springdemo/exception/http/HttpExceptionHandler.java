package pers.cxd.springdemo.exception.http;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public Object rethrowHttpExceptionToClient(HttpServletResponse resp, HttpException e) {
        resp.setStatus(e.httpCode());
        return e.errorBody();
    }

}
