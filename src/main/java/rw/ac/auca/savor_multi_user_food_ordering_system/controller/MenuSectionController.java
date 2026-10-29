package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.MenuSection;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.MenuSectionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-sections")
public class MenuSectionController {

    @Autowired
    private MenuSectionService menuSectionService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody MenuSection menuSection) {
        String message = menuSectionService.save(menuSection);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuSection>> getAll() {
        return ResponseEntity.ok(menuSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<MenuSection> section = menuSectionService.getById(id);
        if (section.isPresent()) {
            return ResponseEntity.ok(section.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The menu section with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody MenuSection menuSection) {
        String result = menuSectionService.update(id, menuSection);
        if ("Menu section updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = menuSectionService.delete(id);
        if ("Menu section deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/unused")
    public ResponseEntity<List<MenuSection>> getUnusedMenuSections() {
        return ResponseEntity.ok(menuSectionService.getUnusedMenuSections());
    }
}

