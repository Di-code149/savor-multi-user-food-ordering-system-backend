package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.KitchenStation;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.MenuSection;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CookRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.KitchenStationRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.MenuSectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CookService {

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private KitchenStationRepository kitchenStationRepository;

    @Autowired
    private MenuSectionRepository menuSectionRepository;

    public String save(Cook cook) {
        if (cook.getKitchenStation() != null && cook.getKitchenStation().getId() != null) {
            kitchenStationRepository.findById(cook.getKitchenStation().getId())
                    .ifPresent(cook::setKitchenStation);
        }
        if (cook.getMenuSections() != null && !cook.getMenuSections().isEmpty()) {
            List<MenuSection> managedSections = new ArrayList<>();
            for (MenuSection ms : cook.getMenuSections()) {
                if (ms.getId() != null) {
                    menuSectionRepository.findById(ms.getId()).ifPresent(managedSections::add);
                }
            }
            cook.setMenuSections(managedSections);
        }
        cookRepository.save(cook);
        return "Cook saved successfully";
    }

    public List<Cook> getAll() {
        return cookRepository.findAll();
    }

    public Optional<Cook> getById(UUID id) {
        return cookRepository.findById(id);
    }

    public String update(UUID id, Cook updated) {
        Optional<Cook> existing = cookRepository.findById(id);
        if (existing.isEmpty()) {
            return "The cook with that id does not exist";
        }
        Cook cook = existing.get();
        cook.setFirstName(updated.getFirstName());
        cook.setLastName(updated.getLastName());
        cook.setPhone(updated.getPhone());

        if (updated.getKitchenStation() != null && updated.getKitchenStation().getId() != null) {
            kitchenStationRepository.findById(updated.getKitchenStation().getId())
                    .ifPresent(cook::setKitchenStation);
        } else if (updated.getKitchenStation() == null) {
            cook.setKitchenStation(null);
        }

        if (updated.getMenuSections() != null) {
            List<MenuSection> managedSections = new ArrayList<>();
            for (MenuSection ms : updated.getMenuSections()) {
                if (ms.getId() != null) {
                    menuSectionRepository.findById(ms.getId()).ifPresent(managedSections::add);
                }
            }
            cook.setMenuSections(managedSections);
        }

        cookRepository.save(cook);
        return "Cook updated successfully";
    }

    public String delete(UUID id) {
        Optional<Cook> existing = cookRepository.findById(id);
        if (existing.isEmpty()) {
            return "The cook with that id does not exist";
        }
        cookRepository.delete(existing.get());
        return "Cook deleted successfully";
    }
}
