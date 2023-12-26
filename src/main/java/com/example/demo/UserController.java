package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public String addNewUser(@RequestBody User user){
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        userRepository.save(newUser);
        return "User signed up successfully!";
    }

    @GetMapping("/byId/{id}")
    public @ResponseBody Optional<User> getUserById(@PathVariable Integer id){
        return userRepository.findById(id);
    }

    @GetMapping("/byFirstName/{name}")
    public @ResponseBody Optional<User> getUserByFirstName(@PathVariable String name){
        return userRepository.findByFirstName(name);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable Integer id){
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }

}
