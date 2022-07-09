package ru.yanddex.practicum.filmorate.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Incorrect id")
public class IncorrectIdToGetException extends Exception{

    public IncorrectIdToGetException(String message){
        super(message);
    }
}
