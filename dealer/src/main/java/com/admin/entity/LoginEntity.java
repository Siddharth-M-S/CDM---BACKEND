package com.admin.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Login")
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private int dealerId;
    private String dealerName;
    private String dealerPassword;
    private String email;
}
