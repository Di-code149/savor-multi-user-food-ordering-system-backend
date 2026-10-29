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

    public List<Cook> getByMenuSection(String name) {
        return cookRepository.findCooksByMenuSectionName(name);
    }

    public String addMenuSection(UUID cookId, UUID menuSectionId) {
        Optional<Cook> cookOpt = cookRepository.findById(cookId);
        if (cookOpt.isEmpty()) {
            return "The cook with that id does not exist";
        }
        Optional<MenuSection> sectionOpt = menuSectionRepository.findById(menuSectionId);
        if (sectionOpt.isEmpty()) {
            return "That menu section does not exist";
        }
        Cook cook = cookOpt.get();
        MenuSection section = sectionOpt.get();
        boolean alreadyHas = cook.getMenuSections().stream()
                .anyMatch(ms -> ms.getId().equals(menuSectionId));
        if (alreadyHas) {
            return "The cook already has that menu section";
        }
        cook.getMenuSections().add(section);
        cookRepository.save(cook);
        return "Menu section added to the cook";
    }

    public String changeKitchenStation(UUID cookId, int stationNumber) {
        Optional<Cook> cookOpt = cookRepository.findById(cookId);
        if (cookOpt.isEmpty()) {
            return "The cook with that id does not exist";
        }
        Optional<KitchenStation> stationOpt = kitchenStationRepository.findByStationNumber(stationNumber);
        if (stationOpt.isEmpty()) {
            return "The kitchen station with that stationNumber does not exist";
        }
        KitchenStation targetStation = stationOpt.get();
        if (cookRepository.existsByKitchenStation(targetStation)) {
            return "That kitchen station is taken by another cook";
        }
        Cook cook = cookOpt.get();
        cook.setKitchenStation(targetStation);
        cookRepository.save(cook);
        return "Kitchen station changed successfully";
    }
}


