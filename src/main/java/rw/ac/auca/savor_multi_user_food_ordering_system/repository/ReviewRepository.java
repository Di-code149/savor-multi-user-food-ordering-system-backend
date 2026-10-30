package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Review;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // Navigate review -> foodOrder -> customer -> username; DESC puts the newest review first
    @Query("SELECT r FROM Review r WHERE r.foodOrder.customer.username = :username ORDER BY r.reviewDate DESC")
    List<Review> findReviewsByCustomerUsername(@Param("username") String username);

    // Used before deleting an order so reviews don't point to a deleted order
    boolean existsByFoodOrder(FoodOrder foodOrder);
}
