package com.matjipdaehak.fo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorizationException extends Exception{
    private String message;

    public AuthorizationException(String message){
        this.message = message;
    }
}
