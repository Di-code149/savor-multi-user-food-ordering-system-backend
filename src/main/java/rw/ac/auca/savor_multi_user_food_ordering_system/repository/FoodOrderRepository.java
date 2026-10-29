package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;

import java.util.UUID;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, UUID> {
}
