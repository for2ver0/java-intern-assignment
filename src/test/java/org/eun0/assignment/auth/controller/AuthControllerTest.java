package org.eun0.assignment.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eun0.assignment.auth.controller.dto.LoginRequest;
import org.eun0.assignment.auth.controller.dto.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("유효한 사용자 정보를 입력하면 회원 가입됩니다.")
    @Test
    void if_you_enter_valid_user_information_you_will_be_registered_as_a_member() throws Exception {
        // given
        SignupRequest request = SignupRequest.of("JIN HO", "12341234", "Mentos");

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("JIN HO"))
                .andExpect(jsonPath("$.nickname").value("Mentos"))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }

    @DisplayName("이미 가입된 사용자 정보를 입력하면 회원 가입되지 않습니다.")
    @Test
    void if_you_enter_already_subscribed_user_information_you_cannot_be_registered_as_a_member() throws Exception {
        // given
        SignupRequest request = SignupRequest.of("JIN HO", "12341234", "Mentos");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("U001"))
                .andExpect(jsonPath("$.message").value("이미 가입된 사용자입니다."));
    }

    @DisplayName("올바른 자격 증명을 입력하면 로그인됩니다.")
    @Test
    void if_you_enter_the_correct_credentials_you_will_logged_in() throws Exception {
        // given
        SignupRequest signupRequest = SignupRequest.of("JIN HO", "12341234", "Mentos");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());

        // when & then
        LoginRequest loginRequest = LoginRequest.of("JIN HO", "12341234");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @DisplayName("잘못된 비밀번호로는 로그인 안 됩니다.")
    @Test
    void cannot_log_in_with_wrong_password() throws Exception {
        // given
        SignupRequest signupRequest = SignupRequest.of("JIN HO", "12341234", "Mentos");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());

        // when & then
        LoginRequest loginRequest = LoginRequest.of("JIN HO", "password");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("A002"))
                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

}