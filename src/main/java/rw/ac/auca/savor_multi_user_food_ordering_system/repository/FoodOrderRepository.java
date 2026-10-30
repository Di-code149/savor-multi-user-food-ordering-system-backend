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
    // The parameter is the enum itself, and the result starts with the earliest order date
    List<FoodOrder> findByStatusOrderByOrderDateAsc(OrderStatus status);

    // Between includes both dates
    List<FoodOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);

    // Used by D1: the cook already has an order on that date that is not cancelled
    boolean existsByCookAndOrderDateAndStatusNot(Cook cook, LocalDate orderDate, OrderStatus status);

    // JPQL GROUP BY returns each status plus its count as an Object[]
    @Query("SELECT o.status, COUNT(o) FROM FoodOrder o GROUP BY o.status")
    List<Object[]> countOrdersPerStatus();

    // One UPDATE query. DELIVERED is passed as a parameter so those rows are never cancelled
    @Modifying
    @Query("UPDATE FoodOrder o SET o.status = :cancelled WHERE o.cook.id = :cookId AND o.orderDate = :date AND o.status <> :delivered")
    int cancelOrdersForCookOnDate(@Param("cookId") UUID cookId, @Param("date") LocalDate date, @Param("cancelled") OrderStatus cancelled, @Param("delivered") OrderStatus delivered);

    long countByOrderDate(LocalDate orderDate);

    long countByStatus(OrderStatus status);
}




