package com.example.demo;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        newUser.setId((int) (userRepository.count() + 1));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

//        Cart cart = new Cart();
//        cart.setUser(newUser);
//        newUser.setCart(cart);
        userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (loginRequest.getPassword().equals(user.getPassword())) {
                // Authentication logic

                // Write user data to a JSON file (for demonstration purposes)
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
            String userDataJson = String.format("{\"id\":\"%d\"," +
                            "\"first_name\":\"%s\"," +
                            "\"last_name\":\"%s\"," +
                            "\"email\":\"%s\"," +
                            "\"comenzi\":\"%d\"," +
                            "\"password\":\"%s\"," +
                            "\"admin\":\"%s\"}",
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

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable Integer id) {
        try {
            // Specify the path to your JSON file
            String jsonFilePath = "userData.json";

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON file and map it to an array of User objects
            List<User> users = objectMapper.readValue(new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));

            // Find the user by ID
            Optional<User> userToDelete = users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst();

            if (userToDelete.isPresent()) {
                // Check admin flag
                if (userToDelete.get().getAdminFlag() == 1) {
                    // Remove the user from the list
                    users.remove(id);
                    return "User deleted successfully!";
                } else {
                    return "Not an admin.";
                }
            } else {
                return "User not found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting user.";
        }
    }
}
