package com.gmail.onishchenko.oleksii.codenames.controller;

import com.gmail.onishchenko.oleksii.codenames.dto.ErrorDto;
import com.gmail.onishchenko.oleksii.codenames.exception.CodenamesException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    private static final Logger log = LogManager.getLogger(AdviceController.class);

    @ExceptionHandler(value = {CodenamesException.class})
    public ResponseEntity<ErrorDto> codenamesException(CodenamesException e) {
        log.debug(e);
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

}
