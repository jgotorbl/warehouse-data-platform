package com.jaime.gotor.warehouse.software.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductStock
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.product.ProductStock</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 20 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock {

    private String productName;
    private Integer quantity;

}
