package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.MenuSection;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.MenuSectionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MenuSectionService {

    @Autowired
    private MenuSectionRepository menuSectionRepository;

    public String save(MenuSection menuSection) {
        menuSectionRepository.save(menuSection);
        return "Menu section saved successfully";
    }

    public List<MenuSection> getAll() {
        return menuSectionRepository.findAll();
    }

    public Optional<MenuSection> getById(UUID id) {
        return menuSectionRepository.findById(id);
    }

    public String update(UUID id, MenuSection updated) {
        Optional<MenuSection> existing = menuSectionRepository.findById(id);
        if (existing.isEmpty()) {
            return "The menu section with that id does not exist";
        }
        MenuSection section = existing.get();
        section.setName(updated.getName());
        menuSectionRepository.save(section);
        return "Menu section updated successfully";
    }

    public String delete(UUID id) {
        Optional<MenuSection> existing = menuSectionRepository.findById(id);
        if (existing.isEmpty()) {
            return "The menu section with that id does not exist";
        }
        menuSectionRepository.delete(existing.get());
        return "Menu section deleted successfully";
    }
}
