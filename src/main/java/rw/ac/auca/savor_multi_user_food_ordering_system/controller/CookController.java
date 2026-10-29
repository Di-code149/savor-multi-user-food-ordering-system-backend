package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.CookService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cooks")
public class CookController {

    @Autowired
    private CookService cookService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Cook cook) {
        String message = cookService.save(cook);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cook>> getAll() {
        return ResponseEntity.ok(cookService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Cook> cook = cookService.getById(id);
        if (cook.isPresent()) {
            return ResponseEntity.ok(cook.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The cook with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Cook cook) {
        String result = cookService.update(id, cook);
        if ("Cook updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = cookService.delete(id);
        if ("Cook deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/by-menu-section")
    public ResponseEntity<List<Cook>> getByMenuSection(@RequestParam String name) {
        return ResponseEntity.ok(cookService.getByMenuSection(name));
    }
}

