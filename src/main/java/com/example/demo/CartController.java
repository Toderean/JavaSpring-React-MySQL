package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/get-items")
    public ResponseEntity<List<Item>> getItemsInCart(@RequestParam Integer userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = user.getCart();
            if (cart == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Item> itemsInCart = cart.getItems();
            return ResponseEntity.ok(itemsInCart);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update-quantity")
    public ResponseEntity<String> updateItemQuantityInCart(
            @RequestParam Integer userId,
            @RequestParam Integer itemId,
            @RequestParam Integer quantity) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = user.getCart();
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart not found");
            }

            List<Item> itemsInCart = cart.getItems();
            for (Item item : itemsInCart) {
                if (item.getId().equals(itemId)) {
                    // Update the quantity for the specified item
                    if(quantity <= item.getCantity()) {
                        item.setCantity(quantity);
                    }
                    if(quantity == 0){
                        itemsInCart.remove(item);
                    }
                    cartRepository.save(cart);
                    return ResponseEntity.ok("Quantity updated successfully");
                }
            }

            return ResponseEntity.badRequest().body("Item not found in the cart");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestParam Integer userId, @RequestParam Integer itemId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            Cart cart = user.getCart();
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                user.setCart(cart);
            }

            cart.getItems().add(item);
            cartRepository.save(cart);


            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
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

    @PostMapping("/finalize-command")
    public ResponseEntity<String> finalizeCommand(@RequestParam Integer userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = user.getCart();
            List<Item> items = (List<Item>) itemRepository.findAll();
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart not found");
            }
            cart.getItems().clear();

            List<Item> itemsInCart = cart.getItems();
//            for (Item item : itemsInCart) {
//                Item temp = items.
//                items.setCantity(updatedQuantity);
//            }

            // Save the updated cart
            cartRepository.save(cart);

            return ResponseEntity.ok("Command finalized successfully. Cart items cleared and quantities updated.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

}