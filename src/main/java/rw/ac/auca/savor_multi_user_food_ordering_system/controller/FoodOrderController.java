package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.FoodOrderService;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/food-orders")
public class FoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    // the service returns a message, so the controller maps it to 201, 400 or 409
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

    @GetMapping("/by-status")
    public ResponseEntity<List<FoodOrder>> getByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(foodOrderService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<FoodOrder>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(foodOrderService.getBetween(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
        String result = foodOrderService.updateStatus(id, status);
        if ("Food order status updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else if ("The food order with that id does not exist".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return ResponseEntity.ok(foodOrderService.getStatsByStatus());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID cookId, @RequestParam String date) {
        LocalDate orderDate = LocalDate.parse(date);
        return ResponseEntity.ok(foodOrderService.cancelDay(cookId, orderDate));
    }

    @GetMapping("/page")
    public ResponseEntity<org.springframework.data.domain.Page<FoodOrder>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderDate,desc") String sort) {
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String property = parts[0];
            org.springframework.data.domain.Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("desc"))
                    ? org.springframework.data.domain.Sort.Direction.DESC
                    : org.springframework.data.domain.Sort.Direction.ASC;
            sortObj = org.springframework.data.domain.Sort.by(direction, property);
        }
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(foodOrderService.getPage(pageable));
    }
}




