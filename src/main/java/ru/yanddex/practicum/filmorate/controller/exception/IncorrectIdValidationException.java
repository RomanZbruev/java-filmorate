package ru.yanddex.practicum.filmorate.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad ID")
public class IncorrectIdValidationException extends IncorrectDateValidationException{
    public IncorrectIdValidationException(String message) {
        super(message);
    }

}
