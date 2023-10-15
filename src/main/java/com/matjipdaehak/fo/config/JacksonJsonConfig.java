package com.matjipdaehak.fo.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson에서 사용되는 json 설정들을 지정한다.
 */
@Data
@Configuration
public class JacksonJsonConfig {
    private String message;
}
