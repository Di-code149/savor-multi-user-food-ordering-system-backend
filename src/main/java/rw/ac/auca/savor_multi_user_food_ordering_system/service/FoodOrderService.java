package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CookRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CustomerRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.FoodOrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FoodOrderService {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CookRepository cookRepository;

    public String save(FoodOrder foodOrder) {
        if (foodOrder.getCustomer() != null && foodOrder.getCustomer().getId() != null) {
            customerRepository.findById(foodOrder.getCustomer().getId())
                    .ifPresent(foodOrder::setCustomer);
        }
        if (foodOrder.getCook() != null && foodOrder.getCook().getId() != null) {
            cookRepository.findById(foodOrder.getCook().getId())
                    .ifPresent(foodOrder::setCook);
        }
        if (foodOrder.getStatus() == null) {
            foodOrder.setStatus(OrderStatus.PENDING);
        }
        foodOrderRepository.save(foodOrder);
        return "Food order saved successfully";
    }

    public List<FoodOrder> getAll() {
        return foodOrderRepository.findAll();
    }

    public Optional<FoodOrder> getById(UUID id) {
        return foodOrderRepository.findById(id);
    }

    public String update(UUID id, FoodOrder updated) {
        Optional<FoodOrder> existing = foodOrderRepository.findById(id);
        if (existing.isEmpty()) {
            return "The food order with that id does not exist";
        }
        FoodOrder order = existing.get();
        if (updated.getOrderDate() != null) {
            order.setOrderDate(updated.getOrderDate());
        }
        if (updated.getDeliveryAddress() != null) {
            order.setDeliveryAddress(updated.getDeliveryAddress());
        }
        order.setTotalAmount(updated.getTotalAmount());
        if (updated.getCustomer() != null && updated.getCustomer().getId() != null) {
            customerRepository.findById(updated.getCustomer().getId())
                    .ifPresent(order::setCustomer);
        }
        if (updated.getCook() != null && updated.getCook().getId() != null) {
            cookRepository.findById(updated.getCook().getId())
                    .ifPresent(order::setCook);
        }
        foodOrderRepository.save(order);
        return "Food order updated successfully";
    }

    public String delete(UUID id) {
        Optional<FoodOrder> existing = foodOrderRepository.findById(id);
        if (existing.isEmpty()) {
            return "The food order with that id does not exist";
        }
        foodOrderRepository.delete(existing.get());
        return "Food order deleted successfully";
    }

    public List<FoodOrder> getByStatus(OrderStatus status) {
        return foodOrderRepository.findByStatusOrderByOrderDateAsc(status);
    }

    public List<FoodOrder> getBetween(java.time.LocalDate start, java.time.LocalDate end) {
        return foodOrderRepository.findByOrderDateBetweenOrderByOrderDateAsc(start, end);
    }
}

