package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.KitchenStation;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.KitchenStationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KitchenStationService {

    @Autowired
    private KitchenStationRepository kitchenStationRepository;

    public String save(KitchenStation kitchenStation) {
        kitchenStationRepository.save(kitchenStation);
        return "Kitchen station saved successfully";
    }

    public List<KitchenStation> getAll() {
        return kitchenStationRepository.findAll();
    }

    public Optional<KitchenStation> getById(UUID id) {
        return kitchenStationRepository.findById(id);
    }

    public String update(UUID id, KitchenStation updated) {
        Optional<KitchenStation> existing = kitchenStationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The kitchen station with that id does not exist";
        }
        KitchenStation station = existing.get();
        station.setStationNumber(updated.getStationNumber());
        station.setName(updated.getName());
        kitchenStationRepository.save(station);
        return "Kitchen station updated successfully";
    }

    public String delete(UUID id) {
        Optional<KitchenStation> existing = kitchenStationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The kitchen station with that id does not exist";
        }
        kitchenStationRepository.delete(existing.get());
        return "Kitchen station deleted successfully";
    }
}
