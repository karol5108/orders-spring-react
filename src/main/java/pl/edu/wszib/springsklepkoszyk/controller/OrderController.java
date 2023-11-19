package pl.edu.wszib.springsklepkoszyk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wszib.springsklepkoszyk.exception.ResourceNotFound;
import pl.edu.wszib.springsklepkoszyk.model.Order;
import pl.edu.wszib.springsklepkoszyk.model.OrderItem;
import pl.edu.wszib.springsklepkoszyk.model.Product;
import pl.edu.wszib.springsklepkoszyk.repository.OrderItemRepo;
import pl.edu.wszib.springsklepkoszyk.repository.OrderRepo;
import pl.edu.wszib.springsklepkoszyk.repository.ProductRepo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private BigDecimal calculateOrderValue(Set<OrderItem> orderItems) {
        BigDecimal orderValue = orderItems.stream().
                                            map(OrderItem::getProductValue).
                                                    reduce(BigDecimal.ZERO, BigDecimal::add);
        return orderValue;

    }

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;


    @GetMapping("")
    public List<Order> getAllOrders() {return orderRepo.findAll();}

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id){
        Order order = orderRepo.findById(id).orElseThrow(() -> new ResourceNotFound("ID not found in db " + id));
        return ResponseEntity.ok(order);
    }

    @PostMapping("/new-order/{id}")
    public ResponseEntity<Order> newOrder(@PathVariable long id){
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFound("ID not found in db "+ id));

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);
        item.setProductValue(product.getPrice());

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(item);

        Order order = new Order();
        order.setCreateDate(Instant.now());
        order.setModifyDate(Instant.now());
        order.setLines(orderItems);
        order.setOrderValue(product.getPrice());

        orderRepo.save(order);

        return ResponseEntity.ok(order);

    }

    @PutMapping("/{orderId}/increment/{productId}")
    public ResponseEntity<Order> incrementItemQuantity(@PathVariable long orderId, @PathVariable long productId){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found " + orderId));

        OrderItem itemToIncrement = order.getLines().stream()
                .filter(orderItem -> orderItem.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Product not found in Order with ID: " + productId));

        int newQuantity = itemToIncrement.getQuantity() + 1;
        itemToIncrement.setQuantity(newQuantity);
        itemToIncrement.setProductValue(itemToIncrement.getProductValue().add(itemToIncrement.getProduct().getPrice()));

        order.setOrderValue(calculateOrderValue(order.getLines()));

        order.setModifyDate(Instant.now());

        orderRepo.save(order);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/decrement/{productId}")
    public ResponseEntity<Order> decrementItemQuantity(@PathVariable long orderId, @PathVariable long productId){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found " + orderId));

        // Sprawdzamy, czy produkt o podanym productId znajduje się w Order
        OrderItem itemToDecrement = order.getLines().stream()
                .filter(orderItem -> orderItem.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Product not found in Order with ID: " + productId));

        int newQuantity = itemToDecrement.getQuantity() - 1;
        if (newQuantity > 0) {
            itemToDecrement.setQuantity(newQuantity);
            itemToDecrement.setProductValue(itemToDecrement.getProductValue().subtract(itemToDecrement.getProduct().getPrice()));
        } else {
            order.getLines().remove(itemToDecrement);
            orderItemRepo.delete(itemToDecrement);
        }

//        if (order.getLines().isEmpty()) {
//            orderRepo.delete(order);
//            return ResponseEntity.noContent().build();
//        } else {
            order.setOrderValue(calculateOrderValue(order.getLines()));
            order.setModifyDate(Instant.now());
            orderRepo.save(order);
      //  }

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/add-product/{productId}")
    public ResponseEntity<Order> addProductToOrder(@PathVariable long orderId, @PathVariable long productId){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found " + orderId));

        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourceNotFound("product not found "  + productId ));

        // CZY W KOSZYKU
        OrderItem existingItem = null;
        for (OrderItem item : order.getLines()) {
            if (item.getProduct().getId() == productId) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // JESLI W KOSZYKU INKREMENTUJ
            int newQuantity = existingItem.getQuantity() + 1;
            existingItem.setQuantity(newQuantity);
            existingItem.setProductValue(existingItem.getProductValue().add(existingItem.getProduct().getPrice()));
        }else{

            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            newItem.setProductValue(product.getPrice());

            order.getLines().add(newItem);
        }
        order.setOrderValue(calculateOrderValue(order.getLines()));
        order.setModifyDate(Instant.now());

        orderRepo.save(order);

        return ResponseEntity.ok(order);

    }

    @PutMapping("/{orderId}/remove-product/{productId}")
    public ResponseEntity<Order> removeProduct(@PathVariable long orderId, @PathVariable long productId){
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFound("order not found " + orderId));

        // Sprawdzamy, czy produkt o podanym productId znajduje się w Order
        OrderItem itemToRemove = order.getLines().stream()
                .filter(orderItem -> orderItem.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Product not found in Order with ID: " + productId));

        order.getLines().remove(itemToRemove);
        orderItemRepo.delete(itemToRemove);

        if (order.getLines().isEmpty()){
                orderRepo.delete(order);
                return ResponseEntity.noContent().build();
        }else{
            order.setOrderValue(calculateOrderValue(order.getLines()));
            order.setModifyDate(Instant.now());
            orderRepo.save(order);
        }

        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/change-quantity/{productId}/{newQuantity}")
    public ResponseEntity<Order> changeQuantity(@PathVariable long orderId, @PathVariable long productId, @PathVariable int newQuantity){
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFound("order not found " + orderId));

        // Sprawdzamy, czy produkt o podanym productId znajduje się w Order
        OrderItem itemToRemove = order.getLines().stream()
                .filter(orderItem -> orderItem.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Product not found in Order with ID: " + productId));

        if (itemToRemove.getQuantity() == newQuantity){
            return ResponseEntity.ok(order);
        }else {
            itemToRemove.setQuantity(newQuantity);
            BigDecimal quantity = BigDecimal.valueOf(newQuantity);
            itemToRemove.setProductValue(itemToRemove.getProduct().getPrice().multiply(quantity));

            order.setOrderValue(calculateOrderValue(order.getLines()));
            order.setModifyDate(Instant.now());

            orderRepo.save(order);
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable long id){
        Order order = orderRepo.findById(id).orElseThrow(() -> new ResourceNotFound("ID not found in db " + id));
        orderRepo.delete(order);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
