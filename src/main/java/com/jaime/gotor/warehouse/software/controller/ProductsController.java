package com.jaime.gotor.warehouse.software.controller;

import com.jaime.gotor.warehouse.software.exception.*;
import com.jaime.gotor.warehouse.software.model.product.ProductCatalogue;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductStock;
import com.jaime.gotor.warehouse.software.service.ProductsService;
import com.jaime.gotor.warehouse.software.service.WarehouseFileReaderService;
import com.jaime.gotor.warehouse.software.validation.DataValidator;
import com.jaime.gotor.warehouse.software.validation.WarehouseFileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * ProductsController
 * <br>
 * <code>com.jaime.gotor.warehouse.software.controller.ProductsController</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 18 February 2022
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final WarehouseFileValidator warehouseFileValidator;
    private final WarehouseFileReaderService<ProductCatalogue> warehouseFileReaderService;
    private final DataValidator<ProductCatalogue> dataValidator;
    private final ProductsService productsService;

    /**
     * Method that loads a file containing data about products being sold in the warehouse.
     *
     * The system loads the file,reads it, validates its data and saves the product data in the system database.
     * We have assumed that every time a file containing products is uploaded the whole set of products is updated.
     *
     * @param productsFile multipart file containing all the product data
     * @return HttpStatus.NO_CONTENT if the request is completed successfully
     *
     * @throws FileValidationException if the file is null, empty or is not a json file
     * @throws IOException if the content of the file is not a valid JSON structure
     * @throws DataValidationException if any the data contained in the file is not valid
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload-products", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> uploadProductCatalogue(MultipartFile productsFile)
            throws FileValidationException, IOException, DataValidationException {
        warehouseFileValidator.validateFile(productsFile);
        ProductCatalogue catalogue = warehouseFileReaderService.readFileContent(productsFile.getBytes(), ProductCatalogue.class);
        Set<ConstraintViolation<ProductCatalogue>> set = dataValidator.validate(catalogue);
        if (!set.isEmpty()) {
            throw new DataValidationException(dataValidator.buildExceptionMessage(set));
        }
        productsService.uploadProductCatalogue(catalogue);
        return ResponseEntity.noContent().build();
    }


    /**
     * For each product, it calculates the maximum quantity of that product that a customer could
     * buy with the existing inventory.
     *
     * @return a list of objects containing the name of the products in their store and the maximum
     * quantity a user could buy with the existing inventory
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get-products-stock", produces = { "application/json" })
    public ResponseEntity<List<ProductStock>> getProductsStocks() {
        return ResponseEntity.ok(productsService.getStockForAllProducts());
    }

    /**
     * Method that sells a product
     *
     * @param product product that we want to sell. This object contains the name of the product and tghe price that
     *                is being paid for it
     * @return returns HttpPStatus.OK containing the product sold
     *
     * @throws NotEnoughQuantityException if there is not enough quantity of any of the articles that the product needs.
     * @throws ProductNotFoundException if that product is not found in the product catalogue
     * @throws NotEnoughFundsException if the amount requested is insufficient to buy the product
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sell-product", consumes = { "application/json" })
    public ResponseEntity<ProductDTO> sellProduct(@RequestBody ProductDTO product) throws NotEnoughQuantityException, ProductNotFoundException, NotEnoughFundsException {
        ProductDTO productDTO = productsService.sellProduct(product);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * Removes a product from the catalogue
     *
     * @param productDTO product that we want to remove from the catalogue
     * @return HttpStatus.NO_CONTENT if the request is completed successfully
     * @throws ProductNotFoundException if the product is not found
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/remove-product", consumes = { "application/json" })
    public ResponseEntity<Void> removeProduct(@RequestBody ProductDTO productDTO) throws ProductNotFoundException {
        productsService.removeProduct(productDTO.getName());
        return ResponseEntity.noContent().build();
    }
}
