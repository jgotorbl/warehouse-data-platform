package com.jaime.gotor.warehouse.software.model.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Article
 * <br>
 * <code>com.jaime.gotor.warehouse.software.model.inventory.Article</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Valid
public class ArticleDTO {

    @JsonProperty("art_id")
    @NotBlank(message = "article if cannot be blank")
    private String articleId;
    @NotBlank(message = "name cannot be blank")
    @JsonProperty("name")
    private String name;
    @NotNull(message = "stock amount is required")
    @PositiveOrZero(message = "stock of article must cannot be negative")
    @JsonProperty("stock")
    private Integer stock;
}
