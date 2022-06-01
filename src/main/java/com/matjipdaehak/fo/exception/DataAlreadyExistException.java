package com.matjipdaehak.fo.exception;

/**
 * 중복되서는 안될 자료가 중복될때 발생시킨다.
 * 예) 이미 DB에 존재하는 가게를 다시 DB에 저장하려 할때.
 * 예) 이미 DB에 존재하는 학교를 DB에 입력하려 할때.
 */
public class DataAlreadyExistException extends RuntimeException{

    public DataAlreadyExistException(String message){
        super(message);
    }
}
