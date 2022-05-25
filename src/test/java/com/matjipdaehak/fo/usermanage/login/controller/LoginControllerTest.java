package com.matjipdaehak.fo.usermanage.login.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    public void authorizationHeaderTest() throws Exception{
        mock.perform(
                MockMvcRequestBuilders.post("/user-management/login")
                        .header("Authorization", "")
                        .content("")
        ).andExpect(status().is4xxClientError());
    }
}
