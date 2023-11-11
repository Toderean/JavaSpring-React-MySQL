package com.example.demo;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item,Integer>{}
