package com.limited.sales.user;

import com.google.gson.Gson;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:/application.properties")
@Transactional
@Slf4j
class UserControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Rollback
    void emailOverlapCheck() throws Exception {
        User user = User.builder()
                .userEmail("ohjeung@naver.com")
                .build();
        Gson gson = new Gson();
        String toJson = gson.toJson(user);
        log.info(toJson);

        mockMvc.perform(get("/user/email")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(toJson)
                )
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @Rollback
    void signUp() throws Exception {
        User user = User.builder()
                .userEmail("ohejung@naver.com")
                .userPassword("1234")
                .useYn("Y")
                .userCellphone("01088887777")
                .userRole("")
                .build();

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @Rollback
    void userInfoUpdate() throws Exception {
        User user = User.builder()
                .userEmail("test@test")
                .userPassword("1234")
                .userCellphone("01088887777")
                .build();

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @Rollback
    void userPasswordChange() throws Exception {
        User user = User.builder()
                .userEmail("test@test")
                .userPassword("1234")
                .build();

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        mockMvc.perform(patch("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @Rollback
    void userDelete() throws Exception {
        User user = User.builder()
                .userEmail("test@test")
                .userPassword("1234")
                .build();

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);

        mockMvc.perform(delete("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }
}