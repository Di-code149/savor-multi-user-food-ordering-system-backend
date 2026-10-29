package rw.ac.auca.savor_multi_user_food_ordering_system.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "kitchen_station")
public class KitchenStation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int stationNumber;

    private String name;

    public KitchenStation() {
    }

    public KitchenStation(UUID id, int stationNumber, String name) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
