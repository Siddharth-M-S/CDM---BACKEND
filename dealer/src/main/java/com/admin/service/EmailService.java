package com.admin.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public boolean sendLoginCredentials(String dealerEmail, String username, String password, Long dealerId) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' indicates multipart email support

            helper.setTo(dealerEmail);
            helper.setSubject("Your Login Credentials");


            String htmlContent = "<html>" +
                    "<body>" +
                    "<h2>Dear Dealer,</h2>" +
                    "<p>Here are your login credentials for our platform <strong>Carquest</strong>:</p>" +
                    "<table>" +
                    "<tr><td><strong>DealerId:</strong></td><td>" + dealerId + "</td></tr>" +
                    "<tr><td><strong>Username:</strong></td><td>" + username + "</td></tr>" +
                    "<tr><td><strong>Password:</strong></td><td>" + password + "</td></tr>" +
                    "</table>" +
                    "<p><em>Please change your password after your first login.</em></p>" +
                    "<br>" +
                    "<p>Best Regards,<br/>Carquest</p>" +
                    "</body>" +
                    "</html>";

            // Set the HTML content
            helper.setText(htmlContent, true); // 'true' indicates HTML content

            // Send the email
            emailSender.send(message);
            logger.debug("Email successfully sent to: {}", dealerEmail);
            return true;

        } catch (MailException | MessagingException e) {
            logger.error("Error sending email to {}: {}", dealerEmail, e.getMessage(), e);
            return false;
        }
    }

    public void orderMessage(String dealerEmail, LocalDateTime orderDate, long orderId,String dealerName) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(dealerEmail);
            helper.setSubject("Order Confirmation for Carquest");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


            String htmlContent = "<html>" +
                    "<body>" +
                    "<h2>Dear "+ dealerName +",</h2>" +
                    "<p>Thank you for placing an order with <strong>Carquest</strong>.</p>" +
                    "<p>Your order is being processed and will be shipped soon. Please review the details below:</p>" +
                    "<table>" +
                    "<tr><td><strong>Order ID:</strong></td><td>O"+ orderId + "</td></tr>" +
                    "<tr><td><strong>Order Date:</strong></td><td>"+ orderDate.format(formatter) +"</td></tr>" +
                    "<tr><td><strong>Status:</strong></td><td>Processing</td></tr>" +
                    "</table>" +
                    "<br>" +
                    "<p>Best Regards,<br/>Carquest Team</p>" +
                    "</body>" +
                    "</html>";

            // Set the HTML content
            helper.setText(htmlContent, true);

            // Send the email
            emailSender.send(message);
            logger.debug("Order confirmation email successfully sent to: {}", dealerEmail);

        } catch (MailException | MessagingException e) {
            logger.error("Error sending order confirmation email to {}: {}", dealerEmail, e.getMessage(), e);
        }
    }



    public void paymentSuccessMessage(String adminEmail, LocalDateTime paymentDate, long orderId, String dealerName, BigDecimal amountPaid, String mail) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(adminEmail);
            helper.setTo(mail);
            helper.setSubject("Payment Successful - Carquest");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String htmlContent = "<html>" +
                    "<body>" +
                    "<h2>Dear "+ dealerName +"</h2>" +
                    "<p>This is to notify you that a payment has been successfully made for an order placed on <strong>Carquest</strong>.</p>" +
                    "<p>Please review the payment details below:</p>" +
                    "<table>" +
                    "<tr><td><strong>Order ID:</strong></td><td>O" + orderId + "</td></tr>" +
                    "<tr><td><strong>Dealer Name:</strong></td><td>" + dealerName + "</td></tr>" +
                    "<tr><td><strong>Payment Date:</strong></td><td>" + paymentDate.format(formatter) + "</td></tr>" +
                    "<tr><td><strong>Amount Paid:</strong></td><td>₹" + amountPaid + "</td></tr>" +
                    "<tr><td><strong>Status:</strong></td><td>Payment Successful</td></tr>" +
                    "</table>" +
                    "<br>" +
                    "<p>Best Regards,<br/>Carquest Team</p>" +
                    "</body>" +
                    "</html>";

            // Set the HTML content
            helper.setText(htmlContent, true);

            // Send the email
            emailSender.send(message);
            logger.debug("Payment success email successfully sent to admin: {}", adminEmail);

        } catch (MailException | MessagingException e) {
            logger.error("Error sending payment success email to {}: {}", adminEmail, e.getMessage(), e);
        }
    }

    public void sendForgotPassword(String email,String newPassword)
    {
        try{
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setTo(email);
            helper.setSubject("Here is your password ");

            String htmlContent = "<html>" +
                    "<body>" +
                    "<h2>Dear Dealer" +"</h2>" +
                    "<p>As requested, here is your new password for your <strong>Carquest</strong> account:</p>" +
                    "<p><strong>New Password:</strong> " + newPassword + "</p>" +
                    "<br>" +
                    "<p>Please make sure to change your password after logging in for security reasons.</p>" +
                    "<br>" +
                    "<p>If you did not request a password reset, please ignore this email or contact support.</p>" +
                    "<br>" +
                    "<p>Best Regards,<br/>Carquest Team</p>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent,true);
            emailSender.send(message);

        }
        catch (MailException | MessagingException e)
        {
            logger.error("Error sending payment success email to  {}",  e.getMessage(), e);

        }

    }





}
