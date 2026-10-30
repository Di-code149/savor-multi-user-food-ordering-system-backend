package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private KitchenStationRepository kitchenStationRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("customers", customerRepository.count());
        summary.put("cooks", cookRepository.count());
        summary.put("freeKitchenStations", (long) kitchenStationRepository.findFreeKitchenStations().size());
        summary.put("ordersToday", foodOrderRepository.countByOrderDate(LocalDate.now()));
        summary.put("pending", foodOrderRepository.countByStatus(OrderStatus.PENDING));
        summary.put("reviews", reviewRepository.count());
        return summary;
    }
}
