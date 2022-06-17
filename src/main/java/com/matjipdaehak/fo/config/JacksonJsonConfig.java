package com.matjipdaehak.fo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson에서 사용되는 json 설정들을 지정한다.
 * ObjectMapper등은 Thread Safe하고 생성비용이 크기 때문에 bean으로 지정한다.
 */
@Configuration
public class JacksonJsonConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
