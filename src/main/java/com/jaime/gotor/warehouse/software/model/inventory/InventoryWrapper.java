package com.jaime.gotor.warehouse.software.model.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * InventoryWrapper
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.inventory.InventoryWrapper</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryWrapper {

    @JsonProperty
    InventoryDTO wrapper;

}
