package ru.yanddex.practicum.filmorate.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Bad release date")
public class IncorrectDateValidationException extends Exception{
    public IncorrectDateValidationException(String message){
        super(message);
    }
}
