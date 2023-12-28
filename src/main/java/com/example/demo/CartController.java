package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestParam Integer userId, @RequestParam Integer itemId) {
        // Implement logic to add an item to the user's cart
        // You might want to check if the user and item exist, and then add the item to the user's cart

        // Example:
         User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
         Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
         Cart cart = cartRepository.findByUser(user);
         cart.getItems().add(item);
         cartRepository.save(cart);

        return ResponseEntity.ok("Item added to cart successfully");
    }

    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(@RequestParam Integer userId) {
        // Retrieve the user's cart based on the user ID
        // Example:
         User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
         Cart cart = cartRepository.findByUser(user);
        // Return the cart or handle the case where the cart is not found
        return ResponseEntity.ok(cart);
    }
}