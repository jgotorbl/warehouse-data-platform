package com.jaime.gotor.warehouse.software.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * Material
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.product.Material</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class MaterialDTO {

    @JsonProperty(value = "art_id")
    @NotBlank(message = "article id must not be blank")
    private String articleId;
    @JsonProperty(value = "amount_of")
    @Positive(message = "amount of material must be positive")
    private Integer amount;
}
