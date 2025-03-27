package com.admin.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

 class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSendLoginCredentials_Success() {
        // Arrange
        String dealerEmail = "dealer@example.com";
        String username = "dealerUser";
        String password = "dealerPass";
        Long dealerId = 123L;

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        boolean result = emailService.sendLoginCredentials(dealerEmail, username, password, dealerId);

        // Assert
        assertTrue(result);
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
     void testSendLoginCredentials_Failure() {
        // Arrange
        String dealerEmail = "dealer@example.com";
        String username = "dealerUser";
        String password = "dealerPass";
        Long dealerId = 123L;

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Mail sending failed") {}).when(emailSender).send(any(MimeMessage.class));

        // Act
        boolean result = emailService.sendLoginCredentials(dealerEmail, username, password, dealerId);

        // Assert
        assertFalse(result);
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
     void testOrderMessage_Success() {
        // Arrange
        String dealerEmail = "dealer@example.com";
        LocalDateTime orderDate = LocalDateTime.now();
        long orderId = 456L;
        String dealerName = "DealerName";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.orderMessage(dealerEmail, orderDate, orderId, dealerName);

        // Assert
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
     void testOrderMessage_Failure() {
        // Arrange
        String dealerEmail = "dealer@example.com";
        LocalDateTime orderDate = LocalDateTime.now();
        long orderId = 456L;
        String dealerName = "DealerName";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Mail sending failed") {}).when(emailSender).send(any(MimeMessage.class));

        // Act
        emailService.orderMessage(dealerEmail, orderDate, orderId, dealerName);

        // Assert
        verify(emailSender, times(1)).send(mimeMessage);
    }
}