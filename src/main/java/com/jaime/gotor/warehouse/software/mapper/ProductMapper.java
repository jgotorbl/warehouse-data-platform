package com.jaime.gotor.warehouse.software.mapper;

import com.jaime.gotor.warehouse.software.database.entity.Material;
import com.jaime.gotor.warehouse.software.database.entity.Product;
import com.jaime.gotor.warehouse.software.model.product.MaterialDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * ProductMapper
 * <br>
 * <code>com.jaime.gotor.warehouse.software.mapper.ProductMapper</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "materialList", source = "materialList")
    })
    Product toProductEntity(ProductDTO product);

    Material toMaterialEntity(MaterialDTO materialDTO);

    ProductDTO toProductDTO(Product product);

    MaterialDTO toMaterialDTO(Material material);



}
