package com.matjipdaehak.fo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JacksonJsonTest {

    private ObjectMapper mapper;
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @BeforeAll
    public void setObjectMapper(){
        mapperBuilder = new Jackson2ObjectMapperBuilder();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void dateMappingTest(){

    }
}
