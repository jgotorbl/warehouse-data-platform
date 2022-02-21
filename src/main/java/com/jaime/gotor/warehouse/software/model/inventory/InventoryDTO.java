package com.jaime.gotor.warehouse.software.model.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Inventory
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.inventory.Inventory</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class InventoryDTO {

    @JsonProperty("inventory")
    @Valid
    @NotNull
    private List<ArticleDTO> inventory;

}
