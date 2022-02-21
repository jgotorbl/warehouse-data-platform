package com.jaime.gotor.warehouse.software.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ProductCatalogue
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.product.ProductCatalogue</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class ProductCatalogue {

    @JsonProperty(value = "products", required = true)
    @NotNull(message = "catalogue must contain products")
    private List<@Valid ProductDTO> productDTOList;

}
