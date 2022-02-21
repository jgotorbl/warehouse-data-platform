package com.jaime.gotor.warehouse.software.service;

import com.jaime.gotor.warehouse.software.database.entity.Product;
import com.jaime.gotor.warehouse.software.database.repository.ProductRepository;
import com.jaime.gotor.warehouse.software.exception.NotEnoughFundsException;
import com.jaime.gotor.warehouse.software.exception.NotEnoughQuantityException;
import com.jaime.gotor.warehouse.software.exception.ProductNotFoundException;
import com.jaime.gotor.warehouse.software.mapper.ProductMapper;
import com.jaime.gotor.warehouse.software.model.inventory.ArticleDTO;
import com.jaime.gotor.warehouse.software.model.product.MaterialDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductCatalogue;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ProductsService
 * <br>
 * <code>com.jaime.gotor.warehouse.software.service.ProductsService</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 18 February 2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsService {

    private static final double EPSILON = 0.000001d;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    /**
     * Method that deletes the existing products from the database and populates the database with the ones
     * loaded from the products file.
     *
     * @param catalogue contains the list of products being uploaded to the database.
     */
    public void uploadProductCatalogue(ProductCatalogue catalogue) {
        productRepository.deleteAll();
        List<com.jaime.gotor.warehouse.software.database.entity.Product> productEntities =
                catalogue.getProductDTOList().stream().map(ProductMapper.MAPPER::toProductEntity).toList();
        productRepository.saveAll(productEntities);
    }


    /**
     * Returns a list of all the products in the database, and for each product the maximum quantity that could be
     * purchased with the existing stock.
     *
     * @return a list of ProductStock objects, each one of them containing the name of the product and the maximum
     * purchasable quantity.
     */
    public List<ProductStock> getStockForAllProducts() {
        List<ProductStock> productStocks = new ArrayList<>();
        productRepository.getAllProducts().forEach((key, value) -> {
            Map<String, ArticleDTO> articlesMap = inventoryService.getInventoryDataMap();
            productStocks.add(getStockForProduct(value, articlesMap));
        });
        return productStocks;
    }

    /**
     * Method that gets a single ProductStock
     *
     * @param productDTO product in the database
     * @param inventory existing inventory
     * @return productStock containing the name of the product and the maximum purchasable quantity
     */
    private ProductStock getStockForProduct(ProductDTO productDTO, Map<String, ArticleDTO> inventory) {
        return new ProductStock(productDTO.getName(), calculateMaxQuantityOfProduct(productDTO, inventory));
    }

    /**
     * @param productDTO product for which we want to calculate the maximum quantity
     * @param inventory existing inventory of the product
     * @return maximum quantity of product available with the existing stock.
     */
    private Integer calculateMaxQuantityOfProduct(ProductDTO productDTO, Map<String, ArticleDTO> inventory) {
        return calculateMaxQuantityOfProduct(productDTO, inventory, 0);
    }

    /**
     * @param productDTO product for which we want to calculate the quantity
     * @param inventory existing inventory. In each recursive call, productDTO material quantities are substracted
     *                  to the stocks of the articles in the inventory
     * @param quantity accumulator that stores the quantity of the product available
     * @return quantity maximum quantity of the product with the existing inventory
     */
    private Integer calculateMaxQuantityOfProduct(ProductDTO productDTO, Map<String, ArticleDTO> inventory, int quantity) {
        if (stockIsEnoughToBuyProduct(productDTO, inventory)) {
            inventoryService.updateInventoryStockForProduct(inventory, productDTO);
            return calculateMaxQuantityOfProduct(productDTO, inventory, quantity + 1);
        }
        return quantity;
    }

    /**
     * Determines if there is enough quantity of a product in stock
     * @param productDTO product for which we want to check the inventory
     * @param inventory existing inventory containing the articles
     * @return true if there is enough quantity to buy a single unit of that product, false otherwise
     */
    private boolean stockIsEnoughToBuyProduct(ProductDTO productDTO, Map<String, ArticleDTO> inventory) {
        List<MaterialDTO> materialDTOList = productDTO.getMaterialList();
        for (MaterialDTO materialDTO : materialDTOList) {
            Integer stockQuantity = Optional.ofNullable(inventory.get(materialDTO.getArticleId()))
                    .map(ArticleDTO::getStock).orElse(0);
            if (stockQuantity < materialDTO.getAmount()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Process the sale of a product
     *
     * @param productToSell product to buy. Contains the name and the amount provided to sell the product
     * @throws NotEnoughQuantityException if the inventory does not contain enough quantity of one or more
     * materials of the product
     * @throws ProductNotFoundException if the product name does not match any of the names of the products
     * in the database
     * @throws NotEnoughFundsException if the amount provided is less than the product price.
     */
    public ProductDTO sellProduct(ProductDTO productToSell) throws NotEnoughQuantityException, ProductNotFoundException, NotEnoughFundsException {
        List<Product> products = productRepository.findByName(productToSell.getName());
        ProductDTO product = products.stream().findFirst()
                .map(ProductMapper.MAPPER::toProductDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product " + productToSell.getName() + " not found"));
        Map<String, ArticleDTO> articleDTOMap = inventoryService.getInventoryDataMap();
        // throw exception is there is not enough quantity of such product
        if (!stockIsEnoughToBuyProduct(product, articleDTOMap)) {
            throw new NotEnoughQuantityException("Stock is not enough to sell " + product.getName());
        }
        if (!hasMoneyToBuyProduct(productToSell.getPrice(), product)) {
            String message = "Not enough money. Quantity " + productToSell.getPrice() + " is less than " + product.getPrice();
            throw new NotEnoughFundsException(message);
        }
        inventoryService.updateInventory(product);
        return product;
    }

    private boolean hasMoneyToBuyProduct(double amount, ProductDTO product) {
        return Math.abs(amount - product.getPrice()) < EPSILON || (amount > product.getPrice());
    }


    /**
     * Removes a single product from the database, if found
     *
     * @param productName name of the product to be removed
     * @throws ProductNotFoundException if the product name does not match any of the products in the database
     */
    public void removeProduct(String productName) throws ProductNotFoundException {
        List<Product> products = productRepository.findByName(productName);
        Product product = products.stream().findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productName + " not found"));
        productRepository.delete(product);
    }
}
