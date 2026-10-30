package rw.ac.auca.savor_multi_user_food_ordering_system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String dishName;

    private String comment;

    private int rating;

    private LocalDate reviewDate;

    //one order can have many reviews
    @ManyToOne
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

    public Review() {
    }

    public Review(UUID id, String dishName, String comment, int rating, LocalDate reviewDate, FoodOrder foodOrder) {
        this.id = id;
        this.dishName = dishName;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.foodOrder = foodOrder;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }
}
