package com.example.demo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    @Autowired
    private final CartRepository cartRepository;
    @Transactional
    public void addItemToCart(Long cartId, Item item) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().add(item);

        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long cartId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
