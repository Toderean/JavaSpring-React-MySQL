package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static reactor.core.Disposables.swap;

@RestController
@RequestMapping("/items")
public class ItemController {   

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/all/filter=0")
        public @ResponseBody Iterable<Item> getAllItems(){
        return itemRepository.findAll();
    }
    @GetMapping("/all/filter=1")
    public @ResponseBody Iterable<Item> getAllItemsFilter(){
        List<Item> aux = new ArrayList<>((Collection) itemRepository.findAll());
        int n = aux.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (aux.get(j + 1).getPrice() < aux.get(j).getPrice()) {
                    Item temp = aux.get(j);
                    aux.set(j, aux.get(j + 1));
                    aux.set(j + 1, temp);
                }
            }
        }
        return aux;
    }

    @PostMapping
    public ResponseEntity<Item> addNewItem(@RequestBody Item item){
        Item n = new Item();
        n.setId((int) (itemRepository.count() + 1));
        n.setName(item.getName());
        n.setPrice(item.getPrice());
        n.setCantity(item.getCantity());
        n.setCategory(item.getCategory());
        n.setPoster(item.getPoster());
        if(item.getTrailerLink().isEmpty())
            n.setTrailerLink("None");
        else
            n.setTrailerLink(item.getTrailerLink());
        itemRepository.save(n);
        return new ResponseEntity<>(n, HttpStatus.CREATED);
    }

    @GetMapping("/item/id={id}")
    public @ResponseBody Optional<Item> getItemById(@PathVariable Integer id){
        return itemRepository.findById(id);
    }

    @GetMapping("/search={name}")
    public @ResponseBody Optional<Iterable<Item>> getItemByName(@PathVariable String name){
        return itemRepository.findItemByName(name);
    }


    @PutMapping("{id}")
    public ResponseEntity<Item> updateItem(@PathVariable("id") Integer id,
                                            @RequestBody Item item){
        Item updatedItem = itemRepository.findById(id).orElseThrow(()-> new RuntimeException());
        updatedItem.setName(item.getName());
        updatedItem.setPoster(item.getPoster());
        updatedItem.setPrice(item.getPrice());
        updatedItem.setTrailerLink(item.getTrailerLink());
        updatedItem.setCantity(item.getCantity());
        updatedItem.setCategory(item.getCategory());
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/admin/deleteItems={id}")
    public @ResponseBody String deleteItem(@PathVariable Integer id){
        itemRepository.deleteById(id);
        return "Succes";
    }
}
