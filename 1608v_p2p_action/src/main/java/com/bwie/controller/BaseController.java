package com.bwie.controller;

import com.bwie.until.FrontStatusConstants;
import com.bwie.until.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: admin
 * @Date: 2019/5/14 11:37
 * @Description:
 */

@Controller
public class BaseController {


    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response HandlerException(HttpServletRequest request, Exception e){
        e.printStackTrace();
       return Response.build().setStatus(FrontStatusConstants.EROO).setMessage(e.getMessage());
    }
}
