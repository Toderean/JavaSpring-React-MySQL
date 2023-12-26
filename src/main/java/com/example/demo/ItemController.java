package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/iteme")
    public @ResponseBody Iterable<Item> getAllItems(){
        return itemRepository.findAll();
    }

    @PostMapping("/admin/addItem")
    public @ResponseBody String addNewItem(@RequestParam String Name,
                                           @RequestParam float price,
                                           @RequestParam Integer Cantity,
                                           @RequestParam String Category,
                                           @RequestParam String ImageURL,
                                           @RequestParam String video){
        Item n = new Item();
        n.setName(Name);
        n.setPrice(price);
        n.setCantity(Cantity);
        n.setCategory(Category);
        n.setPoster(ImageURL);
        if(video.isEmpty())
            n.setTrailerLink("None");
        else
            n.setTrailerLink(video);
        itemRepository.save(n);
        return "Succes";
    }

    @GetMapping("/item/id={id}")
    public @ResponseBody Optional<Item> getItemById(Integer id){
        return itemRepository.findById(id);
    }

    @GetMapping("/search={name}")
    public @ResponseBody Optional<Iterable<Item>> getItemByName(@PathVariable String name){
        return itemRepository.findItemByName(name);
    }

    @DeleteMapping("/admin/deleteItems={id}")
    public @ResponseBody String deleteItem(@PathVariable Integer id){
        itemRepository.deleteById(id);
        return "Succes";
    }
}
