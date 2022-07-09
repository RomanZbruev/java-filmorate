package ru.yanddex.practicum.filmorate.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Bad ID")
public class IncorrectIdValidationException extends Exception{
    public IncorrectIdValidationException(String message) {
        super(message);
    }

}
