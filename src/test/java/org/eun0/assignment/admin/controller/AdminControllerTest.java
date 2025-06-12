package org.eun0.assignment.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eun0.assignment.auth.controller.dto.LoginRequest;
import org.eun0.assignment.auth.controller.dto.SignupRequest;
import org.eun0.assignment.auth.entity.Member;
import org.eun0.assignment.auth.respository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("관리자 권한을 가진 사용자가 다른 사용자에게 관리자 권한을 부여할 수 있습니다.")
    @Test
    void admin_can_promote_user_to_admin() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("adminPassword");
        Member testAdmin = Member.of("testAdmin", encodedPassword, "testAdmin");
        testAdmin.promoteToAdmin();
        memberRepository.save(testAdmin);

        LoginRequest loginRequest = LoginRequest.of("testAdmin", "adminPassword");
        MvcResult adminLogin = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = adminLogin.getResponse().getContentAsString();
        String adminToken = objectMapper.readTree(responseContent).get("token").asText();

        SignupRequest userSignupRequest = SignupRequest.of("JIN HO", "12341234", "Mentos");
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Member testUser = memberRepository.findByUsername("JIN HO").orElseThrow();
        Long testUserId = testUser.getId();

        // when & then
        mockMvc.perform(patch("/admin/users/" + testUserId + "/roles")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JIN HO"))
                .andExpect(jsonPath("$.nickname").value("Mentos"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[?(@=='ADMIN')]").exists());
    }

    @DisplayName("일반 사용자가 다른 사용자에게 관리자 권한을 부여할 수 없습니다.")
    @Test
    void user_cannot_promote_user_to_admin() throws Exception {
        // given
        SignupRequest user1SignupRequest = SignupRequest.of("user1", "12341234", "Mentos");
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1SignupRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SignupRequest user2SignupRequest = SignupRequest.of("user2", "12341234", "Haribo");
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2SignupRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        LoginRequest loginRequest = LoginRequest.of("user1", "12341234");
        MvcResult user1Login = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = user1Login.getResponse().getContentAsString();
        String user1Token = objectMapper.readTree(responseContent).get("token").asText();

        Member user2 = memberRepository.findByUsername("user2").orElseThrow();
        Long user2Id = user2.getId();

        // when & then
        mockMvc.perform(patch("/admin/users/" + user2Id + "/roles")
                        .header("Authorization", user1Token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("A001"))
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }
}