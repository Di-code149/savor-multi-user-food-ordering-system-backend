package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Review;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.FoodOrderRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.ReviewRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    public String save(UUID foodOrderId, Review review) {
        Optional<FoodOrder> orderOpt = foodOrderRepository.findById(foodOrderId);
        if (orderOpt.isEmpty()) {
            return "The food order with that id does not exist";
        }
        FoodOrder order = orderOpt.get();
        if (order.getStatus() != OrderStatus.DELIVERED) {
            return "Reviews are only for DELIVERED food orders";
        }
        review.setReviewDate(LocalDate.now());
        review.setFoodOrder(order);
        reviewRepository.save(review);
        return "Review saved successfully";
    }

    public List<Review> getCustomerReviews(String username) {
        return reviewRepository.findReviewsByCustomerUsername(username);
    }

    public String delete(UUID id) {
        Optional<Review> existing = reviewRepository.findById(id);
        if (existing.isEmpty()) {
            return "The review with that id does not exist";
        }
        reviewRepository.delete(existing.get());
        return "Review deleted successfully";
    }
}
