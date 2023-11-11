package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laba")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public @ResponseBody Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public @ResponseBody String addNewUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email){
        User n = new User();
        n.setFirstName(firstName);
        n.setLastName(lastName);
        n.setEmail(email);
        userRepository.save(n);
        return "Succes";
    }

}
