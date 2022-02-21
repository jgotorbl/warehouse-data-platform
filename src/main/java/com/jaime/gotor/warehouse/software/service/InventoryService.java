package com.jaime.gotor.warehouse.software.service;


import com.jaime.gotor.warehouse.software.database.entity.Article;
import com.jaime.gotor.warehouse.software.database.repository.ArticlesRepository;
import com.jaime.gotor.warehouse.software.mapper.InventoryMapper;
import com.jaime.gotor.warehouse.software.model.inventory.ArticleDTO;
import com.jaime.gotor.warehouse.software.model.inventory.InventoryDTO;
import com.jaime.gotor.warehouse.software.model.product.MaterialDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ArticlesService
 * <br>
 * <code>com.jaime.gotor.warehouse.software.service.ArticlesService</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 18 February 2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ArticlesRepository articlesRepository;

    /**
     * Method that deletes all the existing articles in the database and stores the ones uploaded
     * @param data contains all the inventory data.
     */
    public void loadInventoryData(InventoryDTO data) {
        articlesRepository.deleteAll();
        List<com.jaime.gotor.warehouse.software.database.entity.Article> articleEntities = data.getInventory().stream().map(InventoryMapper.MAPPER::toArticleEntity).toList();
        articlesRepository.saveAll(articleEntities);
    }

    /**
     * Method that returns all the articles in stock in a map. Article entities are retrieved from
     * the database and then mapped into DTOs.
     *
     * @return returns a map containing the articles' data. The values of the map are the articles,
     * and the keys the article IDs
     */
    public Map<String, ArticleDTO> getInventoryDataMap() {
        return articlesRepository.findAll().stream().map(InventoryMapper.MAPPER::toArticleDTO)
                .collect(Collectors.toMap(ArticleDTO::getArticleId, Function.identity()));
    }

    /**
     * Method that updates the inventory table with the new data. The method gets the existing
     * inventory, updates the amount of stock given that the product has been sold and saves the new
     * status in the database.
     *
     * @param productDTO product that we want to sell
     */
    public void updateInventory(ProductDTO productDTO) {
        Map<String, ArticleDTO> articleDTOMap = this.getInventoryDataMap();
        this.updateInventoryStockForProduct(articleDTOMap, productDTO);
        List<Article> articleList = articleDTOMap.values().stream().map(InventoryMapper.MAPPER::toArticleEntity).toList();
        articlesRepository.saveAll(articleList);
    }

    /**
     * Method that updates the existing inventory. Product material quantities are subtracted to the
     * stock of each article.
     *
     * @param inventory map of articles to update
     * @param productDTO the product that we are using to update the stock
     */
    public void updateInventoryStockForProduct(Map<String, ArticleDTO> inventory, ProductDTO productDTO) {
        for (MaterialDTO materialDTO : productDTO.getMaterialList()) {
            ArticleDTO articleDTO = inventory.get(materialDTO.getArticleId());
            articleDTO.setStock(articleDTO.getStock() - materialDTO.getAmount());
            inventory.put(materialDTO.getArticleId(), articleDTO);
        }
    }
}
