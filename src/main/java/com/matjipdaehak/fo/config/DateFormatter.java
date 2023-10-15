package com.matjipdaehak.fo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * String을 Date로 변환해주는 객체 모음
 */
@Configuration
public class DateFormatter {

    @Bean(name = "yyyy-mm-dd")
    public SimpleDateFormat yyyyMmDd(){
        return new SimpleDateFormat("yyyy-mm-dd");
    }
}
