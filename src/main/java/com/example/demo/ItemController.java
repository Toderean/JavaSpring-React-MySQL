package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/muie")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public @ResponseBody Iterable<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public @ResponseBody String addNewItem(@RequestParam String Name, @RequestParam float price){
        Item n = new Item();
        n.setName(Name);
        n.setPrice(price);
        itemRepository.save(n);
        return "Succes";
    }

//    public String allItems(){
//        return "Muie";
//    }
}
