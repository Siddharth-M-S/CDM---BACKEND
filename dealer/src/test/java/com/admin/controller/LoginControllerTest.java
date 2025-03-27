package com.admin.controller;

import com.admin.dto.ForgotPasswordRequest;
import com.admin.entity.DealerInfoEntity;
import com.admin.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private MockMvc mockMvc;

    private DealerInfoEntity dealerInfoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        // Sample DealerInfoEntity object
        dealerInfoEntity = new DealerInfoEntity();
        dealerInfoEntity.setId(1L);
        dealerInfoEntity.setUsername("Dealer1");
        dealerInfoEntity.setEmail("dealer1@example.com");
        dealerInfoEntity.setPhoneNumber(Long.valueOf("1234567890"));
    }

    @Test
    void testGetDealer() throws Exception {
        List<DealerInfoEntity> dealers = Arrays.asList(dealerInfoEntity);
        when(loginService.getDealer()).thenReturn(dealers);

        mockMvc.perform(get("/api/login/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].username").value("Dealer1"));
    }

    @Test
    void testSaveDealer_Success() throws Exception {
        when(loginService.saveDealer(any(DealerInfoEntity.class))).thenReturn("Dealer added successfully");

        mockMvc.perform(post("/api/login/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Dealer1\",\"email\":\"dealer1@example.com\",\"phoneNumber\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dealer added successfully"));
    }

    @Test
    void testSaveDealer_InternalServerError() throws Exception {
        when(loginService.saveDealer(any(DealerInfoEntity.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/login/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Dealer1\",\"email\":\"dealer1@example.com\",\"phoneNumber\":\"1234567890\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testValidateDealer() throws Exception {
        when(loginService.validateDealer(any(DealerInfoEntity.class))).thenReturn("Login Successful");

        mockMvc.perform(post("/api/login/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Dealer1\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login Successful"));
    }

    @Test
    void testDeleteDealer_Success() throws Exception {
        when(loginService.deleteDealer(1L)).thenReturn("Dealer deleted successfully");

        mockMvc.perform(delete("/api/login/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dealer deleted successfully"));
    }

    @Test
    void testDeleteDealer_InternalServerError() throws Exception {
        when(loginService.deleteDealer(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/api/login/delete/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testForgotPassword() throws Exception {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setOldPassword("oldPassword123");
        forgotPasswordRequest.setNewPassword("newPassword123");
        forgotPasswordRequest.setDealerId(1L);

        when(loginService.resetPassword("oldPassword123", 1L, "newPassword123"))
                .thenReturn("Password reset successfully");

        mockMvc.perform(put("/api/login/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"oldPassword123\",\"newPassword\":\"newPassword123\",\"dealerId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

    @Test
    void testUpdateDealer() throws Exception {
        DealerInfoEntity updatedDealer = new DealerInfoEntity();
        updatedDealer.setId(1L);
        updatedDealer.setUsername("UpdatedDealer");
        updatedDealer.setEmail("updated@example.com");
        updatedDealer.setPhoneNumber(Long.valueOf("9876543210"));

        when(loginService.updateDealer(eq(1L), any(DealerInfoEntity.class))).thenReturn(updatedDealer);

        mockMvc.perform(put("/api/login/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"UpdatedDealer\",\"email\":\"updated@example.com\",\"phoneNumber\":\"9876543210\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedDealer"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"));
    }
}
