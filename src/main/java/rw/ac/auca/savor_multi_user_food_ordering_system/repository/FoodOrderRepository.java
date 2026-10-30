package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;

import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, UUID> {
    List<FoodOrder> findByStatusOrderByOrderDateAsc(OrderStatus status);
    List<FoodOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);
    boolean existsByCookAndOrderDateAndStatusNot(Cook cook, LocalDate orderDate, OrderStatus status);

    @Query("SELECT o.status, COUNT(o) FROM FoodOrder o GROUP BY o.status")
    List<Object[]> countOrdersPerStatus();

    @Modifying
    @Query("UPDATE FoodOrder o SET o.status = :cancelled WHERE o.cook.id = :cookId AND o.orderDate = :date AND o.status <> :delivered")
    int cancelOrdersForCookOnDate(@Param("cookId") UUID cookId, @Param("date") LocalDate date, @Param("cancelled") OrderStatus cancelled, @Param("delivered") OrderStatus delivered);

    long countByOrderDate(LocalDate orderDate);

    long countByStatus(OrderStatus status);
}




