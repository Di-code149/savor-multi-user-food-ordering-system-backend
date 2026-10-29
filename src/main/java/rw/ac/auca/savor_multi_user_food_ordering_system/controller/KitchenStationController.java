package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.KitchenStation;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.KitchenStationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/kitchen-stations")
public class KitchenStationController {

    @Autowired
    private KitchenStationService kitchenStationService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody KitchenStation kitchenStation) {
        String message = kitchenStationService.save(kitchenStation);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<KitchenStation>> getAll() {
        return ResponseEntity.ok(kitchenStationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<KitchenStation> station = kitchenStationService.getById(id);
        if (station.isPresent()) {
            return ResponseEntity.ok(station.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The kitchen station with that id does not exist");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody KitchenStation kitchenStation) {
        String result = kitchenStationService.update(id, kitchenStation);
        if ("Kitchen station updated successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = kitchenStationService.delete(id);
        if ("Kitchen station deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
