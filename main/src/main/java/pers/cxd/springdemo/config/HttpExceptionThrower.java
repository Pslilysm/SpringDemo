package pers.cxd.springdemo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class HttpExceptionThrower {

    @ExceptionHandler(value = HttpStatusCodeException.class)
    @ResponseBody
    public String throwClientError(HttpServletResponse resp, HttpStatusCodeException e) {
        resp.setStatus(e.getStatusCode().value());
        return e.getMessage();
    }

}
