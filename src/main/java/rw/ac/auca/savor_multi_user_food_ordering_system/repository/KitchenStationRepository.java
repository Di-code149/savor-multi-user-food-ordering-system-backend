package rw.ac.auca.savor_multi_user_food_ordering_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.KitchenStation;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KitchenStationRepository extends JpaRepository<KitchenStation, UUID> {
    @Query("SELECT k FROM KitchenStation k WHERE NOT EXISTS (SELECT c FROM Cook c WHERE c.kitchenStation = k) ORDER BY k.stationNumber ASC")
    List<KitchenStation> findFreeKitchenStations();

    Optional<KitchenStation> findByStationNumber(int stationNumber);
}


