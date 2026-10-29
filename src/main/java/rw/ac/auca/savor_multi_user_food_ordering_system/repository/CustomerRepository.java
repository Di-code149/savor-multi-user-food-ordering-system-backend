package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT o.customer FROM FoodOrder o WHERE o.cook.id = :cookId")
    List<Customer> findCustomersByCookId(@Param("cookId") UUID cookId);
}


