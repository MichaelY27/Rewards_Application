package com.example.reward.aop;


import com.example.reward.domain.dto.ResponseDTO;
import com.example.reward.domain.exceptions.OperationUnableToProcessException;
import com.example.reward.domain.exceptions.TransactionNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    /**
     * Handle the exceptions related to transaction not found
     */
    @ExceptionHandler(value = {TransactionNotExistException.class})
    public ResponseEntity<ResponseDTO> handleTransactionNotExistException(TransactionNotExistException e){
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Error")
                                        .message(e.getMessage()).build()
                                        ,HttpStatus.NOT_FOUND);
    }

    /**
     * Handle the exceptions that the syntax of the request is correct but the server couldn't process it
     */
    @ExceptionHandler(value = {OperationUnableToProcessException.class})
    public ResponseEntity<ResponseDTO> handleTransactionModificationNotAllowedException(OperationUnableToProcessException e){
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Error")
                                        .message(e.getMessage()).build()
                                        ,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle the exceptions related to invalid request parameters
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Error")
                .message(errors).build()
                ,HttpStatus.BAD_REQUEST);

    }

    /**
     * Handle other possible exceptions related to client side errors
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, HttpRequestMethodNotSupportedException.class, MethodArgumentTypeMismatchException.class, ConstraintViolationException.class, IllegalStateException.class})
    public ResponseEntity<ResponseDTO> handleClientSideException(Exception e){
        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Error")
                                        .message(e.getMessage()).build()
                                        ,HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle the other general exceptions might be server side errors
     */
//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<ResponseDTO> handleException(Exception e){
//        return new ResponseEntity<>(ResponseDTO.builder().timestamp(Instant.now()).messageType("Error")
//                .message(e.getClass().getName() + ": " +e.getMessage()).build()
//                ,HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
