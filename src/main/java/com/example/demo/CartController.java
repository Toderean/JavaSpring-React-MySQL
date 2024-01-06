package com.example.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

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
            String jsonFilePath = "userData.json";
            ObjectMapper objectMapper = new ObjectMapper();

            List<User> users = objectMapper.readValue(
                    new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class)
            );

            // Find the user by ID
            User user = users.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Cart userCart = user.getCart();
            if (userCart != null) {
                List<Item> itemsInCart = userCart.getItems();
                return ResponseEntity.ok(itemsInCart);
            } else {
                return ResponseEntity.ok(List.of());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update-quantity")
    public ResponseEntity<String> updateItemQuantityInCart(
            @RequestParam Integer itemId,
            @RequestParam Integer quantity) {
        try {
            String jsonFilePath = "userData.json";
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> users = objectMapper.readValue(
                    new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class)
            );
            Optional<User> currentUser = userRepository.findById(users.get(0).getId());

            Cart cart = currentUser.get().getCart();
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart not found");
            }

            List<Item> itemsInCart = cart.getItems();
            for (Item item : itemsInCart) {
                if (item.getId().equals(itemId)) {
                    if (quantity <= item.getCantity()) {
                        item.setCantity(quantity);
                    }
                    if (quantity == 0) {
                        itemsInCart.remove(item);
                    }
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
    public ResponseEntity<String> addToCart(@RequestParam Integer itemId) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
            String userData = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
        return null;
    }
    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(@RequestParam Integer userId) {
         User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
         Cart cart = cartRepository.findByUser(user);
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