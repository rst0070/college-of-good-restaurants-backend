package com.matjipdaehak.fo.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
/**
 * EMAIL_AUTH_CODE 테이블에 대응되는 객체
 */
@Data
@NoArgsConstructor
public class EmailAuthCode {
    private String emailAddr;
    private String authCode;
    private Date expDate;

    public EmailAuthCode(
            String emailAddr,
            String authCode,
            Date expDate
    ){
        this.emailAddr = emailAddr;
        this.authCode = authCode;
        this.expDate = expDate;
    }
}
