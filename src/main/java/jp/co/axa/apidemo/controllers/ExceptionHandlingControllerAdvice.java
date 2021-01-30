package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.ErrorInfoDTO;
import jp.co.axa.apidemo.exceptions.LockingException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;



@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    /**
     * This handles ResourceNotFoundExceptions and creates a response object for them
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ErrorInfoDTO resourceNotFoundResponse(HttpServletRequest req, Exception ex) {
        ErrorInfoDTO response = new ErrorInfoDTO(req.getRequestURL().toString(), ex);
        return response;
    }


    /**
     * This handles OptimisticLockingExceptions and creates a response object for them
     */
    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(LockingException.class)
    @ResponseBody
    public ErrorInfoDTO optimisticLockingExceptionResponse(HttpServletRequest req, Exception ex) {
        ErrorInfoDTO response = new ErrorInfoDTO(req.getRequestURL().toString(), ex);
        return response;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfoDTO generic(HttpServletRequest req, Exception ex) {
        ErrorInfoDTO response = new ErrorInfoDTO(req.getRequestURL().toString(), ex);
        return response;
    }



}
