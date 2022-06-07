package com.myhotels.hotelinventory.advice;

import com.fasterxml.classmate.util.ConcurrentTypeCache;
import com.myhotels.hotelinventory.exception.HotelInventoryNotFoundException;
import com.myhotels.hotelinventory.exception.RoomInventoryNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidRequest(MethodArgumentNotValidException ex) {
        Map<String, String> errMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errMap.put(err.getField(), err.getDefaultMessage());
        });
        return errMap;
    }

    @ExceptionHandler(HotelInventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleHotelInventoryNotFoundException(HotelInventoryNotFoundException ex) {
        return Map.of("errorMessage", ex.getMessage());
    }

    @ExceptionHandler(RoomInventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleRoomInventoryNotFoundException(RoomInventoryNotFoundException ex) {
        return Map.of("errorMessage", ex.getMessage());
    }

}
