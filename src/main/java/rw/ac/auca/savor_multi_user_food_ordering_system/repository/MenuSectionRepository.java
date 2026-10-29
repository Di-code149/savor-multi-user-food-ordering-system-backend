package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.MenuSection;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, UUID> {
    @Query("SELECT m FROM MenuSection m WHERE m.cooks IS EMPTY")
    List<MenuSection> findUnusedMenuSections();
}

