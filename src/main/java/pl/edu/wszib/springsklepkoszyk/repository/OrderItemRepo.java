package pl.edu.wszib.springsklepkoszyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wszib.springsklepkoszyk.model.OrderItem;


public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}