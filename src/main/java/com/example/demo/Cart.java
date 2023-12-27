package com.example.demo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@Table(name = "Cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Basic
    Map<Item,Integer> objects = new HashMap<>();

    private Integer clientID;

    public float getSum(){
        float sum = 0;
        for (Map.Entry<Item,Integer> set : objects.entrySet()){
            sum += set.getKey().getPrice() * set.getValue();
        }
        return sum;
    }

    public void addItem(Item item,Integer quantity){
        objects.put(item,quantity);
    }
}
