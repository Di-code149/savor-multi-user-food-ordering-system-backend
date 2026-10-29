package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.MenuSection;

import java.util.UUID;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, UUID> {
}
