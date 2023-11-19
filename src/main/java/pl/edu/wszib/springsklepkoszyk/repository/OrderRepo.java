package pl.edu.wszib.springsklepkoszyk.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wszib.springsklepkoszyk.model.Order;
import pl.edu.wszib.springsklepkoszyk.model.Product;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    /// ++

}