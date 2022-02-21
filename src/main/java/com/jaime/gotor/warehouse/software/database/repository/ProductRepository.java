package com.jaime.gotor.warehouse.software.database.repository;

import com.jaime.gotor.warehouse.software.database.entity.Product;
import com.jaime.gotor.warehouse.software.mapper.ProductMapper;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ProductRepository
 * <br>
 * <code>com.jaime.gotor.warehouse.software.database.repository.ProductRepository</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Gets all products in the database
     * @return a DTO containing all product data
     */
    default Map<String, ProductDTO> getAllProducts() {
        return findAll().stream().map(ProductMapper.MAPPER::toProductDTO)
                .collect(Collectors.toUnmodifiableMap(ProductDTO::getName, Function.identity()));
    }

    List<Product> findByName(String name);

}
