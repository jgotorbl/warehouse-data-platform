package com.jaime.gotor.warehouse.software.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Product
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.product.Product</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class ProductDTO {

    @JsonProperty(value = "name")
    @NotBlank(message = "Name must not be blank or null")
    private String name;
    @JsonProperty(value = "price")
    @PositiveOrZero(message = "Price must not be negative")
    private double price;
    @JsonProperty(value = "contain_articles")
    @NotNull(message = "product must have materials")
    private List<@Valid MaterialDTO> materialList;

}
