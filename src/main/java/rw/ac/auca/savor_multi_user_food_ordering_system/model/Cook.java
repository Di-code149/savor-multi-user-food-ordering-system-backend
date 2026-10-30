package rw.ac.auca.savor_multi_user_food_ordering_system.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cook")
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "kitchen_station_id", nullable = true)
    private KitchenStation kitchenStation;

    //cook owns the many-to-many , so saving a cook writes the rows into cook_menu_section
    @ManyToMany
    @JoinTable(
        name = "cook_menu_section",
        joinColumns = @JoinColumn(name = "cook_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_section_id")
    )
    private List<MenuSection> menuSections = new ArrayList<>();

    public Cook() {
    }

    public Cook(UUID id, String firstName, String lastName, String phone, KitchenStation kitchenStation, List<MenuSection> menuSections) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.kitchenStation = kitchenStation;
        this.menuSections = menuSections;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public KitchenStation getKitchenStation() {
        return kitchenStation;
    }

    public void setKitchenStation(KitchenStation kitchenStation) {
        this.kitchenStation = kitchenStation;
    }

    public List<MenuSection> getMenuSections() {
        return menuSections;
    }

    public void setMenuSections(List<MenuSection> menuSections) {
        this.menuSections = menuSections;
    }
}
