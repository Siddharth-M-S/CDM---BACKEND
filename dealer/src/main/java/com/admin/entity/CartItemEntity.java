package com.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Entity

@Data

@NoArgsConstructor

@AllArgsConstructor

public class CartItemEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne

    @JoinColumn(name = "car_id")

    private Car car;

    private Integer quantity;

    @ManyToOne

    @JoinColumn(name = "cart_id")

    @JsonIgnore

    private CartEntity cart;

}

