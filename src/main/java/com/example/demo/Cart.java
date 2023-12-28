package com.example.demo;

import com.example.demo.Item;
import com.example.demo.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartId")
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "Cart_Items",
            joinColumns = @JoinColumn(name = "CartId"),
            inverseJoinColumns = @JoinColumn(name = "ItemId")
    )
    private List<Item> items;

    // Additional properties and methods as needed
}