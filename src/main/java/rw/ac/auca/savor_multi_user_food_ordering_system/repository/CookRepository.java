package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.ac.auca.savor_multi_user_food_ordering_system.model.KitchenStation;

import java.util.List;
import java.util.UUID;

@Repository
public interface CookRepository extends JpaRepository<Cook, UUID> {
    // join is needed because menuSections is a collection; LOWER makes the search case-insensitive
    @Query("SELECT c FROM Cook c JOIN c.menuSections m WHERE LOWER(m.name) = LOWER(:name)")
    List<Cook> findCooksByMenuSectionName(@Param("name") String name);

    boolean existsByKitchenStation(KitchenStation kitchenStation);
}


