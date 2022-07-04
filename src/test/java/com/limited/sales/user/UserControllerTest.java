package com.limited.sales.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limited.sales.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 테스트코드 수정중~~~~~~
 */
@Slf4j
@WebAppConfiguration
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private String access_token;

    /*
     @Autowired
     UserMapper userMapper;
     */


    @BeforeEach
    @DisplayName("토큰 발급")
    void getUserToken() throws Exception {
        //파라미터 셋팅
        User user = new User();
        user.setUserEmail("abcd@naver.com");
        user.setUserPassword("1234");

        String json = objectMapper.writeValueAsString(user);

        log.debug("=============== joinInfo : {}", json);

        access_token = mockMvc.perform(post("/login")
                        .header("Authorization", "limited")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getHeader("Authorization");
    }

    /*
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


     */

    @Test
    @DisplayName("회원가입")
    @Rollback
    void join() throws Exception {

        //파라미터 셋팅
        User user = new User();
        user.setUserEmail("abcdeee@naver.com");
        user.setUserPassword("1234");
        user.setUserCellphone("01012341234");
        user.setUserRole("ROLE_USER");
        user.setUseYn("Y");

        //userMapper.saveUser(user);
        //User findUser = userMapper.findByUserEmail(user.getUserEmail());
        //assertThat(user.getUserEmail()).isEqualTo(findUser.getUserEmail());


        String json = objectMapper.writeValueAsString(user);

        log.debug("=============== joinInfo : {}", json);

        mockMvc.perform(post("/user")
                        .header("Authorization", "limited")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("로그인")
    void user() throws Exception {
        //파라미터 셋팅
        User user = new User();
        user.setUserEmail("abcd@naver.com");
        user.setUserPassword("1234");

        String json = objectMapper.writeValueAsString(user);

        log.debug("=============== joinInfo : {}", json);

        mockMvc.perform(post("/login")
                        .header("Authorization", "limited")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("이메일중복체크")
    void userEmailChk() throws Exception {
        //파라미터 셋팅
        User user = new User();
        user.setUserEmail("abcd@naver.com");

        String json = objectMapper.writeValueAsString(user);

        log.debug("=============== joinInfo : {}", json);

        mockMvc.perform(get("/user/email")
                        .header("Authorization", "limited")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }


}