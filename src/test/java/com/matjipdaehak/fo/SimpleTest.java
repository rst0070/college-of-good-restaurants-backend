package com.matjipdaehak.fo;

import com.matjipdaehak.fo.config.JacksonJsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SimpleTest {

    @Autowired
    JacksonJsonConfig jsonConfig;

    @Test
    public void test1(){
        jsonConfig.setMessage("asd");
        System.out.println(jsonConfig.getMessage());
        Assertions.assertEquals("asd", jsonConfig.getMessage());
    }

    @Test
    public void test2(){
        System.out.println(jsonConfig.getMessage());
        Assertions.assertEquals("asd", jsonConfig.getMessage());
    }

}
