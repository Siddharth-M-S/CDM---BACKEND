package com.admin.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

import java.util.ArrayList;

import java.util.List;

@Entity

@Data

@NoArgsConstructor

@AllArgsConstructor

public class CartEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne

    @JoinColumn(name = "dealer_id")

    private DealerInfoEntity dealerInfoEntity;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)

    private List<CartItemEntity> items = new ArrayList<>();


}
