package com.example.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;

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
    public ResponseEntity<List<Item>> getItemsInCart() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
        Integer id = 0;
        String userJson = reader.readLine();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(userJson));
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();
            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                parser.nextToken();
                switch (fieldName){
                    case "id":id = parser.getValueAsInt(); break;
                    default: break;
                }
            }
        }
        parser.close();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("id-ul userului "+ user.getId());

        Cart cart = user.getCart();
        System.out.println("cart id " + cart.getCartId());
//        CartService cartService = new CartService(cartRepository);
        System.out.println(cart.getItems());
        if(cart.getItems() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else return new ResponseEntity<>(cart.getItems(),HttpStatus.ACCEPTED);
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

    @PostMapping("/add-to-cart/{itemId}")
    public ResponseEntity<String> addToCart(@PathVariable Integer itemId) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
        Integer id =0;
        String userJson = reader.readLine();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(userJson));
        System.out.println(userJson);
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                parser.nextToken(); // Move to the value
                switch (fieldName) {
                    case "id":  id = parser.getValueAsInt();
                                System.out.println(fieldName + ": " + id);
                                break;
                    default:
                        break;
                }
            }
        }
        parser.close();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("id-ul userului " + user.getId());

        Cart cart = user.getCart();
        CartService cartsrv = new CartService(cartRepository);


        System.out.println("id-ul cartului " + cart.getCartId());

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        System.out.println("size ul cartului inainte " + cart.cartSize());

        cartsrv.addItemToCart(cart.getCartId(),item);

        System.out.println("size ul cartului dupa" + cart.cartSize());
        System.out.println("Cartul are:" + cart.getItems() + cart.getCartId());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(@RequestParam Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/finalize-command")
    public ResponseEntity<String> finalizeCommand() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
            Integer id =0;
            String userJson = reader.readLine();
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(new StringReader(userJson));
            System.out.println(userJson);
            while(!parser.isClosed()){
                JsonToken jsonToken = parser.nextToken();
                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken(); // Move to the value
                    switch (fieldName) {
                        case "id":  id = parser.getValueAsInt();
                            System.out.println(fieldName + ": " + id);
                            break;
                        default:
                            break;
                    }
                }
            }
            parser.close();
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            Cart cart = user.getCart();
            CartService cartService = new CartService(cartRepository);
            List<Item> items = cart.getItems();
            for(Item product: items){
               product.setCantity(product.getCantity() - 1);
               itemRepository.save(product);
            }
            cartService.clearCart(cart.getCartId());

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}