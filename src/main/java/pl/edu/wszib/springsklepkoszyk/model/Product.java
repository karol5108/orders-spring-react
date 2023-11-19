package pl.edu.wszib.springsklepkoszyk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // autoinkementacja dla id, primary key
    private long id;

    // pozostale, nie moga byc puste
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;


}
