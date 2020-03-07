package com.redhat.cloudnative.inventory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author boonp
 * @since 07/03/2020 4:05 PM
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {

    private InventoryRepository inventoryRepository;


    public InventoryRestController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Inventory getAvailability(@PathVariable String itemId) {
        Optional<Inventory> inventory = inventoryRepository.findById(itemId);

        return inventory.orElseGet(() -> {
            Inventory item = new Inventory();
            item.setItemId(itemId);
            item.setQuantity(0);
            return item;
        });
    }


}
