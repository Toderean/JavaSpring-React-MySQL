package com.example.demo;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item,Integer>{
    Optional<Item> findItemByName(String name);
}
