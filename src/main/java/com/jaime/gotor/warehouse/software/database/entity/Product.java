package com.jaime.gotor.warehouse.software.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Product
 * <br>
 * <code>com.jaime.gotor.warehouse.software.database.entity.Product</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private double price;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Material> materialList;

}
