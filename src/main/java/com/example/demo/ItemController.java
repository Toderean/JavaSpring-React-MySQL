package com.example.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import java.io.FileReader;
import java.io.IOException;
import java.io.*;

import static reactor.core.Disposables.swap;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all/filter=0")
    public @ResponseBody Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/all/filter=1")
    public @ResponseBody Iterable<Item> getAllItemsFilter() {
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

    @PostMapping()
    public ResponseEntity<Item> addNewItem(@RequestBody Item item) {
        Item n = new Item();
        n.setId((int) (itemRepository.count() + 1));
        n.setName(item.getName());
        n.setPrice(item.getPrice());
        n.setCantity(item.getCantity());
        n.setCategory(item.getCategory());
        n.setPoster(item.getPoster());
        if (item.getTrailerLink().isEmpty())
            n.setTrailerLink("None");
        else
            n.setTrailerLink(item.getTrailerLink());
        itemRepository.save(n);
        return new ResponseEntity<>(n, HttpStatus.CREATED);
    }

    @GetMapping("/item/id={id}")
    public @ResponseBody Optional<Item> getItemById(@PathVariable Integer id) {
        return itemRepository.findById(id);
    }

    @GetMapping("/search={name}")
    public @ResponseBody Optional<Iterable<Item>> getItemByName(@PathVariable String name) {
        return itemRepository.findItemByName(name);
    }


    @PostMapping("/act/id={id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id,
                                           @RequestBody Item item) {
        System.out.println(item.toString());
        try {
            Item updatedItem = itemRepository.findById(id).orElse(null);
            if (updatedItem != null) {
                updatedItem.setName(item.getName());
                updatedItem.setPoster(item.getPoster());
                updatedItem.setPrice(item.getPrice());
                updatedItem.setTrailerLink(item.getTrailerLink());
                updatedItem.setCantity(item.getCantity());
                updatedItem.setCategory(item.getCategory());
                itemRepository.save(updatedItem);
                return ResponseEntity.ok(updatedItem);
            } else {
                addNewItem(item);
                return ResponseEntity.ok(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/admin/deleteItems={id}")
    public ResponseEntity<String> deleteItem(@PathVariable Integer id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("userData.json"));
        Integer idUser = 0;

        String userJson = reader.readLine();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new StringReader(userJson));

        System.out.println(userJson);

        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = parser.getCurrentName();
                parser.nextToken();
                switch (fieldName) {
                    case "id":
                        idUser = parser.getValueAsInt();
                        break;
                    default:
                        break;
                }
            }
        }
        parser.close();
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAdminFlag() == 1){
            itemRepository.deleteById(id);
            return ResponseEntity.ok("Item deleted");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
