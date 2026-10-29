package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;

import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, UUID> {
    List<FoodOrder> findByStatusOrderByOrderDateAsc(OrderStatus status);
    List<FoodOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);
    boolean existsByCookAndOrderDateAndStatusNot(Cook cook, LocalDate orderDate, OrderStatus status);
}


