package rw.ac.auca.savor_multi_user_food_ordering_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Review;
import rw.ac.auca.savor_multi_user_food_ordering_system.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID foodOrderId, @RequestBody Review review) {
        String result = reviewService.save(foodOrderId, review);
        if ("Review saved successfully".equals(result)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else if ("The food order with that id does not exist".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if ("Reviews are only for DELIVERED food orders".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/customer/{username}")
    public ResponseEntity<List<Review>> getCustomerReviews(@PathVariable String username) {
        return ResponseEntity.ok(reviewService.getCustomerReviews(username));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = reviewService.delete(id);
        if ("Review deleted successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
