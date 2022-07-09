package ru.yanddex.practicum.filmorate.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Have no like from this user")
public class NotWithLikeException extends Exception{
    public NotWithLikeException(String message){
        super(message);
    }
}
