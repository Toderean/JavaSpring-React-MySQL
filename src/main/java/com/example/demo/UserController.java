package com.example.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        User newUser = new User();
        System.out.println(user.toString());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        System.out.println(user.getEmail());
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getPassword());
        newUser.setId((int) (userRepository.count() + 1));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());


        Cart cart = new Cart();
        cart.setCartId(Long.valueOf(newUser.getId()));// Set the cart ID to the user's command ID
        newUser.setIdComanda(Math.toIntExact(cart.getCartId()));
        cart.setUser(newUser); // Set the user for the cart
        newUser.setCart(cart); // Set the cart for the user

        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (loginRequest.getPassword().equals(user.getPassword())) {
                writeUserDataToFile(user);
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();

            // Return an error response to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }


    private void writeUserDataToFile(User user) {
        try (FileWriter fileWriter = new FileWriter("userData.json")) {
            String userDataJson = String.format("{\"id\":%d," +
                            "\"first_name\":\"%s\"," +
                            "\"last_name\":\"%s\"," +
                            "\"email\":\"%s\"," +
                            "\"comenzi\":%d," +
                            "\"password\":\"%s\"," +
                            "\"admin\":%d}",
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getIdComanda(),
                    user.getPassword(),
                    user.getAdminFlag());
            fileWriter.write(userDataJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/byId/{id}")
    public @ResponseBody Optional<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @GetMapping("/byFirstName/{name}")
    public @ResponseBody Optional<User> getUserByFirstName(@PathVariable String name) {
        return userRepository.findByFirstName(name);
    }


    @PostMapping("/update/id={id}")
    public ResponseEntity<User> updateUserById(@PathVariable Integer id,
                                               @RequestBody User user) {
        System.out.println("Aici e userul"+ user.toString());
        try {
            User toUpdate = userRepository.findById(id).orElse(null);

            if (toUpdate != null) {
                toUpdate.setFirstName(user.getFirstName());
                toUpdate.setLastName(user.getLastName());
                toUpdate.setEmail(user.getEmail());
                toUpdate.setPassword(user.getPassword());
                toUpdate.setAdminFlag(user.getAdminFlag());

                userRepository.save(toUpdate);
                return ResponseEntity.ok(toUpdate);
            } else {
//                addNewUser(user);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminPanel() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
        Integer idUser = 0;
        String userJson = reader.readLine();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(userJson));
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                parser.nextToken();
                switch (fieldName) {
                    case "id":
                        idUser = parser.getValueAsInt();
                        break;
                    default:
                        break;
                }
            }
        }
        parser.close();
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAdminFlag() == 1){
            return ResponseEntity.ok("Item deleted");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
        Integer idUser = 0;
        String userJson = reader.readLine();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(userJson));
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                parser.nextToken();
                switch (fieldName) {
                    case "id":
                        idUser = parser.getValueAsInt();
                        break;
                    default:
                        break;
                }
            }
        }
        parser.close();
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAdminFlag() == 1){
            userRepository.deleteById(id);
            return ResponseEntity.ok("Item deleted");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
