package com.admin.service;

import com.admin.entity.DealerInfoEntity;
import com.admin.exception.CustomException;
import com.admin.repository.DealerInfoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private DealerInfoRepository dealerInfoRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private EmailService emailService;

    private DealerInfoEntity dealerInfoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dealerInfoEntity = new DealerInfoEntity();
        dealerInfoEntity.setId(1L);
        dealerInfoEntity.setUsername("testUser");
        dealerInfoEntity.setEmail("test@example.com");
        dealerInfoEntity.setPhoneNumber(Long.valueOf("1234567890"));
        dealerInfoEntity.setPassword(new BCryptPasswordEncoder().encode("password123"));
    }

    @Test
    void testSaveDealer_Success() {
        when(passwordService.generateRandomPassword()).thenReturn("randomPassword");
        when(dealerInfoRepository.save(any(DealerInfoEntity.class))).thenReturn(dealerInfoEntity);
        when(emailService.sendLoginCredentials(anyString(), anyString(), anyString(), anyLong())).thenReturn(true);

        String result = loginService.saveDealer(dealerInfoEntity);

        assertEquals("Dealer added successfully", result);
        verify(dealerInfoRepository, times(1)).save(any(DealerInfoEntity.class));
        verify(emailService, times(1)).sendLoginCredentials(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    void testSaveDealer_EmailFailure() {
        when(passwordService.generateRandomPassword()).thenReturn("randomPassword");
        when(dealerInfoRepository.save(any(DealerInfoEntity.class))).thenReturn(dealerInfoEntity);
        when(emailService.sendLoginCredentials(anyString(), anyString(), anyString(), anyLong())).thenReturn(false);

        String result = loginService.saveDealer(dealerInfoEntity);

        assertEquals("Dealer Not added Successfully", result);
        verify(dealerInfoRepository, times(1)).deleteById(dealerInfoEntity.getId());
    }

    @Test
    void testValidateDealer_Success() {
        when(dealerInfoRepository.findById(1L)).thenReturn(Optional.of(dealerInfoEntity));

        DealerInfoEntity inputDealer = new DealerInfoEntity();
        inputDealer.setId(1L);
        inputDealer.setUsername("testUser");
        inputDealer.setPassword("password123");

        String result = loginService.validateDealer(inputDealer);

        assertEquals("Login Successful", result);
    }

    @Test
    void testValidateDealer_Failure() {
        when(dealerInfoRepository.findById(1L)).thenReturn(Optional.of(dealerInfoEntity));

        DealerInfoEntity inputDealer = new DealerInfoEntity();
        inputDealer.setId(1L);
        inputDealer.setUsername("wrongUser");
        inputDealer.setPassword("wrongPassword");

        String result = loginService.validateDealer(inputDealer);

        assertEquals("Login Credential Failed", result);
    }

    @Test
    void testDeleteDealer_Success() {
        doNothing().when(dealerInfoRepository).deleteById(1L);

        String result = loginService.deleteDealer(1L);

        assertEquals("Dealer deleted successfully", result);
        verify(dealerInfoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDealer_Failure() {
        doThrow(new RuntimeException("Error")).when(dealerInfoRepository).deleteById(1L);

        Exception exception = assertThrows(CustomException.class, () -> loginService.deleteDealer(1L));
        assertTrue(exception.getMessage().contains("Error during delete Operation"));
    }
}
