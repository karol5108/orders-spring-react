package pl.edu.wszib.springsklepkoszyk.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wszib.springsklepkoszyk.exception.ResourceNotFound;
import pl.edu.wszib.springsklepkoszyk.model.Product;
import pl.edu.wszib.springsklepkoszyk.repository.ProductRepo;



import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {
   @Autowired
    private ProductRepo productRepo;

    // GET - wyswietlanie wszystkich produktow
    @GetMapping("")
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }


    @PostMapping("/add-new-product")
    public ResponseEntity<Product> addNewProduct(@RequestBody Product newProduct){
        productRepo.save(newProduct);
        return ResponseEntity.ok(newProduct);
    }


    // POST - dodaj 10 produktow
    @PostMapping("/add-10")
    public ResponseEntity<String> add10(){
        productRepo.addProducts();
        return ResponseEntity.ok("added-10");
    }

    // GET - znajdz produkt po id
    @GetMapping("{id}")
    public ResponseEntity<Product> getProdByID(@PathVariable long id){
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("ID not found in db " + id));

        return ResponseEntity.ok(product);
    }

    // DELETE - usuwanie produktu
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteProd(@PathVariable long id){
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("ID not found in db " + id));
        productRepo.delete(product);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // PUT - update produktu (zmiana nazwy,ceny)
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProd(@PathVariable long id, @RequestBody Product prodUp){
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("ID not found in db " + id));

        product.setName(prodUp.getName());
        product.setPrice(prodUp.getPrice());

        productRepo.save(product);

        return ResponseEntity.ok(product);
    }
}
