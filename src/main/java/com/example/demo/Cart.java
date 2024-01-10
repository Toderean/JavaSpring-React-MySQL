package com.example.demo;

import com.example.demo.Item;
import com.example.demo.User;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Cart")
@NoArgsConstructor
@AllArgsConstructor

public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartId")
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "Cart_Items",
            joinColumns = @JoinColumn(name = "CartId"),
            inverseJoinColumns = @JoinColumn(name = "ItemId")
    )
    private final List<Item> items = new ArrayList<>();

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }



    public void addItems(Item item){
        int position = items.size();
        items.add(position,item);
    }

    public int cartSize(){
        return items.size();
    }
}


