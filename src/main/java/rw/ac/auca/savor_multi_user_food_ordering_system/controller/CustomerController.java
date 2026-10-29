package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Customer;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.CustomerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        String message = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Customer> customer = customerService.getById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The customer with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Customer customer) {
        String result = customerService.update(id, customer);
        if ("Customer updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = customerService.delete(id);
        if ("Customer deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Customer>> getByLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(customerService.getByLastName(lastName));
    }
}

