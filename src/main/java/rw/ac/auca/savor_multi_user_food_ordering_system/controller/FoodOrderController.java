package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.FoodOrderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/food-orders")
public class FoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody FoodOrder foodOrder) {
        String result = foodOrderService.save(foodOrder);
        if ("Food order saved successfully".equals(result)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else if ("Food order date cannot be in the past".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else if ("Cook already has an order on that date".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FoodOrder>> getAll() {
        return ResponseEntity.ok(foodOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<FoodOrder> order = foodOrderService.getById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The food order with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody FoodOrder foodOrder) {
        String result = foodOrderService.update(id, foodOrder);
        if ("Food order updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = foodOrderService.delete(id);
        if ("Food order deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else if ("That food order has reviews, delete them first".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
