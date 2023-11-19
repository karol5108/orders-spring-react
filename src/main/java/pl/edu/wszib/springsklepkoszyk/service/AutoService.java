package pl.edu.wszib.springsklepkoszyk.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import pl.edu.wszib.springsklepkoszyk.repository.OrderRepo;
import pl.edu.wszib.springsklepkoszyk.repository.ProductRepo;

@Service
public class AutoService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @PostConstruct
    public void initializeProducts(){
        productRepo.addProducts();
    }


}
