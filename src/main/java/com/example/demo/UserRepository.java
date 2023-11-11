package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer>{
    Optional<User> findByFirstName(String name);
}
