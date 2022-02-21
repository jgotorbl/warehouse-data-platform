package com.jaime.gotor.warehouse.software.mapper;

import com.jaime.gotor.warehouse.software.database.entity.Article;
import com.jaime.gotor.warehouse.software.model.inventory.ArticleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * InventoryMapper
 * <br>
 * <code>com.jaime.gotor.warehouse.software.mapper.InventoryMapper</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Mapper
public interface InventoryMapper {

    InventoryMapper MAPPER = Mappers.getMapper(InventoryMapper.class);

    Article toArticleEntity(ArticleDTO article);

    ArticleDTO toArticleDTO(Article article);

}
