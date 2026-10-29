package rw.ac.auca.savor_multi_user_food_ordering_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.ac.auca.savor_multi_user_food_ordering_system.model.Customer;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CookRepository;
import rw.ac.auca.savor_multi_user_food_ordering_system.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CookRepository cookRepository;


    public String save(Customer customer) {
        customerRepository.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        return customerRepository.findById(id);
    }

    public String update(UUID id, Customer updated) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }
        Customer customer = existing.get();
        customer.setFirstName(updated.getFirstName());
        customer.setLastName(updated.getLastName());
        customer.setUsername(updated.getUsername());
        customer.setDeliveryAddress(updated.getDeliveryAddress());
        customerRepository.save(customer);
        return "Customer updated successfully";
    }

    public String delete(UUID id) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }
        customerRepository.delete(existing.get());
        return "Customer deleted successfully";
    }

    public List<Customer> getByLastName(String lastName) {
        return customerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public Optional<List<Customer>> getCustomersOfCook(UUID cookId) {
        if (!cookRepository.existsById(cookId)) {
            return Optional.empty();
        }
        return Optional.of(customerRepository.findCustomersByCookId(cookId));
    }
}


