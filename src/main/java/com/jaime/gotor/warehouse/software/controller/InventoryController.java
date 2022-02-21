package com.jaime.gotor.warehouse.software.controller;

import com.jaime.gotor.warehouse.software.exception.DataValidationException;
import com.jaime.gotor.warehouse.software.exception.FileValidationException;
import com.jaime.gotor.warehouse.software.model.inventory.InventoryDTO;
import com.jaime.gotor.warehouse.software.service.InventoryService;
import com.jaime.gotor.warehouse.software.service.WarehouseFileReaderService;
import com.jaime.gotor.warehouse.software.validation.DataValidator;
import com.jaime.gotor.warehouse.software.validation.WarehouseFileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Set;

/**
 * ArticlesController
 * <br>
 * <code>com.jaime.gotor.warehouse.software.controller.ArticlesController</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 17 February 2022
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final WarehouseFileValidator warehouseFileValidator;
    private final WarehouseFileReaderService<InventoryDTO> warehouseFileReaderService;
    private final DataValidator<InventoryDTO> dataValidator;
    private final InventoryService inventoryService;


    /**
     * Method that loads a file containing data about articles being stored in the warehouse.
     *
     * The system loads the file,reads it, validates its data and saves the inventory data in the system database.
     * We have assumed that every time a file is uploaded all the stock is updated.
     *
     * @param inventoryFile multipart file containing all the articles data
     * @return HttpStatus.NO_CONTENT if the request is completed successfully
     *
     * @throws FileValidationException if the file is null, empty or is not a json file
     * @throws IOException if the content of the file is not a valid JSON structure
     * @throws DataValidationException if any the data contained in the file is not valid
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload-articles", produces = { "text/plain" }, consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadArticles(@RequestPart MultipartFile inventoryFile) throws FileValidationException, IOException, DataValidationException {
        warehouseFileValidator.validateFile(inventoryFile);
        InventoryDTO data = warehouseFileReaderService.readFileContent(inventoryFile.getBytes(), InventoryDTO.class);
        Set<ConstraintViolation<InventoryDTO>> violationSet = dataValidator.validate(data);
        if (!violationSet.isEmpty()) {
            throw new DataValidationException(dataValidator.buildExceptionMessage(violationSet));
        }
        inventoryService.loadInventoryData(data);
        return ResponseEntity.noContent().build();
    }
}
