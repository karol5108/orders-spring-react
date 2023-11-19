package pl.edu.wszib.springsklepkoszyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wszib.springsklepkoszyk.model.Product;

import java.math.BigDecimal;


public interface ProductRepo extends JpaRepository<Product, Long> {

    // + metody poza JpaRepository
     default void addProducts() {
        for (int i= 0; i< 10; i++){
            Product product = new Product();
            product.setName("Product " + i);
            BigDecimal price = new BigDecimal(10.99 + i * i);
            product.setPrice(price);
            this.save(product);
        }

}
}