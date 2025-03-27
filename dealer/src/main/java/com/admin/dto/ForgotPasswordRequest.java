package com.admin.dto;


import lombok.Data;

@Data
public class ForgotPasswordRequest {



    private Long dealerId;
    private String oldPassword;
    private String newPassword;


}
