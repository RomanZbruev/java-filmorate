package ru.yanddex.practicum.filmorate.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Not in friends")
public class NotInFriendsException extends Exception{
    public NotInFriendsException(String message){
        super(message);
    }
}
