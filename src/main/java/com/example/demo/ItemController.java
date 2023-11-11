package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/muie")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/muist")
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

    @GetMapping("/{id}")
    public @ResponseBody Optional<Item> getItemById(Integer id){
        return itemRepository.findById(id);
    }

    @GetMapping("/{name}")
    public @ResponseBody Optional<Iterable<Item>> getItemByName(@PathVariable String name){
        return itemRepository.findItemByName(name);
    }

    public @ResponseBody String deleteItem(Integer id){
        itemRepository.deleteById(id);
        return "Succes";
    }
//    public String allItems(){
//        return "Muie";
//    }
}
