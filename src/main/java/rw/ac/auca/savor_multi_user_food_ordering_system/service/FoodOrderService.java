package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.FoodOrder;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.OrderStatus;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CookRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CustomerRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.FoodOrderRepository;

import java.time.LocalDate;
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

    @Autowired
    private rw.ac.auca.savor_multi_user_food_ordering_system.repository.ReviewRepository reviewRepository;


    // D1: reject past dates first, then check the cook's day, then force PENDING
    public String save(FoodOrder foodOrder) {
        if (foodOrder.getOrderDate() != null && foodOrder.getOrderDate().isBefore(LocalDate.now())) {
            return "Food order date cannot be in the past";
        }
        if (foodOrder.getCook() != null && foodOrder.getCook().getId() != null) {
            Optional<rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook> cookOpt = cookRepository.findById(foodOrder.getCook().getId());
            if (cookOpt.isPresent()) {
                rw.ac.auca.savor_multi_user_food_ordering_system.model.Cook cook = cookOpt.get();
                foodOrder.setCook(cook);
                if (foodOrderRepository.existsByCookAndOrderDateAndStatusNot(cook, foodOrder.getOrderDate(), OrderStatus.CANCELLED)) {
                    return "Cook already has an order on that date";
                }
            }
        }
        if (foodOrder.getCustomer() != null && foodOrder.getCustomer().getId() != null) {
            customerRepository.findById(foodOrder.getCustomer().getId())
                    .ifPresent(foodOrder::setCustomer);
        }
        // Force PENDING so the client cannot create a DELIVERED order directly
        foodOrder.setStatus(OrderStatus.PENDING);
        foodOrderRepository.save(foodOrder);
        return "Food order saved successfully";
    }

    // D2: only PENDING -> PREPARING/CANCELLED and PREPARING -> DELIVERED/CANCELLED are allowed
    public String updateStatus(UUID id, OrderStatus newStatus) {
        Optional<FoodOrder> existing = foodOrderRepository.findById(id);
        if (existing.isEmpty()) {
            return "The food order with that id does not exist";
        }
        FoodOrder order = existing.get();
        OrderStatus current = order.getStatus();

        boolean allowed = false;
        if (current == OrderStatus.PENDING && (newStatus == OrderStatus.PREPARING || newStatus == OrderStatus.CANCELLED)) {
            allowed = true;
        } else if (current == OrderStatus.PREPARING && (newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELLED)) {
            allowed = true;
        }

        // DELIVERED and CANCELLED are final, so anything else goes to the error message
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        order.setStatus(newStatus);
        foodOrderRepository.save(order);
        return "Food order status updated successfully";
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

    //IF REVIEWS exist, deleting the order would leave orphan reviews, so i block it here
    public String delete(UUID id) {
        Optional<FoodOrder> existing = foodOrderRepository.findById(id);
        if (existing.isEmpty()) {
            return "The food order with that id does not exist";
        }
        if (reviewRepository.existsByFoodOrder(existing.get())) {
            return "That food order has reviews, delete them first";
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

    public List<Object[]> getStatsByStatus() {
        return foodOrderRepository.countOrdersPerStatus();
    }

    @org.springframework.transaction.annotation.Transactional
    public String cancelDay(UUID cookId, LocalDate date) {
        int count = foodOrderRepository.cancelOrdersForCookOnDate(cookId, date, OrderStatus.CANCELLED, OrderStatus.DELIVERED);
        return count + " food orders cancelled";
    }

    public org.springframework.data.domain.Page<FoodOrder> getPage(org.springframework.data.domain.Pageable pageable) {
        return foodOrderRepository.findAll(pageable);
    }
}



