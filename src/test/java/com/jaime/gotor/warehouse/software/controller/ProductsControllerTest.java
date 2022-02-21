package com.jaime.gotor.warehouse.software.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaime.gotor.warehouse.software.WarehouseSoftwareApplication;
import com.jaime.gotor.warehouse.software.database.entity.Article;
import com.jaime.gotor.warehouse.software.database.entity.Product;
import com.jaime.gotor.warehouse.software.database.repository.ArticlesRepository;
import com.jaime.gotor.warehouse.software.database.repository.ProductRepository;
import com.jaime.gotor.warehouse.software.mapper.InventoryMapper;
import com.jaime.gotor.warehouse.software.mapper.ProductMapper;
import com.jaime.gotor.warehouse.software.model.inventory.ArticleDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductDTO;
import com.jaime.gotor.warehouse.software.model.product.ProductStock;
import com.jaime.gotor.warehouse.software.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ProductsControllerTest
 * <br>
 * <code>com.jaime.gotor.warehouse.software.controller.ProductsControllerTest</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 21 February 2022
 */
@SpringBootTest(classes = WarehouseSoftwareApplication.class)
public class ProductsControllerTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ArticlesRepository articlesRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        List<Product> productList = new ArrayList<>();
        when(productRepository.saveAll(anyCollection())).thenReturn(productList);
        when(articlesRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
    }

    @Test
    void productController_whenValidProductsFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/products.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile("productsFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, fileResource.getInputStream().readAllBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-products")
                        .file(inventoryFile)).andExpect(status().is(204));
        verify(productRepository).saveAll(anyCollection());
    }

    @Test
    void productController_whenInvalidProductsFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/products_invalid.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile("productsFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, fileResource.getInputStream().readAllBytes());

        assertNotNull(inventoryFile.getBytes());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-products")
                        .file(inventoryFile))
                .andExpect(status().is(400));
        verify(productRepository, never()).saveAll(anyCollection());
    }

    @Test
    void productController_whenEmptyFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/products_invalid.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile("productsFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, "".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-products")
                        .file(inventoryFile)).andExpect(status().is(400));
        verify(productRepository, never()).saveAll(anyCollection());
    }

    @Test
    void productsController_getProducts() throws Exception {
        List<ProductDTO> products = objectMapper.readValue(new DefaultResourceLoader().getResource("files/product_list.json")
                .getInputStream(), new TypeReference<>() {});
        Map<String, ProductDTO> productDTOMap = products.stream().collect(Collectors.toMap(ProductDTO::getName, Function.identity()));
        when(productRepository.getAllProducts()).thenReturn(productDTOMap);
        InventoryService inventoryService = Mockito.mock(InventoryService.class);
        when(inventoryService.getInventoryDataMap()).thenReturn(Collections.emptyMap());
        doCallRealMethod().when(inventoryService).updateInventoryStockForProduct(anyMap(), any(ProductDTO.class));
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult result = mockMvc.perform(get("/get-products-stock")).andExpect(status().isOk()).andReturn();
        List<ProductStock> productStock = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(2, productStock.size());
        assertTrue(productStock.stream().allMatch(product -> product.getQuantity() == 0));
    }

    @Test
    void productsController_whenProductIsFoundAndThereIsStock_sellProduct() throws Exception {
        final String PRODUCT_NAME = "Dining Chair";
        ProductDTO productToSell = new ProductDTO();
        productToSell.setName("Dining Chair");
        List<ProductDTO> products = objectMapper.readValue(new DefaultResourceLoader().getResource("files/product_list.json")
                .getInputStream(), new TypeReference<>() {});
        List<ArticleDTO> articleDTOList = objectMapper.readValue(new DefaultResourceLoader().getResource("files/articles_list.json")
                .getInputStream(), new TypeReference<>() {});
        List<Article> articleList = articleDTOList.stream().map(InventoryMapper.MAPPER::toArticleEntity).toList();
        when(productRepository.findByName(productToSell.getName()))
                .thenReturn(products.stream().map(ProductMapper.MAPPER::toProductEntity).toList());
        when(articlesRepository.findAll()).thenReturn(articleList);
        when(articlesRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult result = mockMvc.perform(post("/sell-product").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productToSell))).andExpect(status().isOk()).andReturn();
        ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(PRODUCT_NAME, productDTO.getName());
    }

    @Test
    void productsController_whenProductIsNotFound_returnBadResponse() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("");
        List<ProductDTO> products = objectMapper.readValue(new DefaultResourceLoader().getResource("files/product_list.json")
                .getInputStream(), new TypeReference<>() {});
        Map<String, ProductDTO> productDTOMap = products.stream().collect(Collectors.toMap(ProductDTO::getName, Function.identity()));
        when(productRepository.getAllProducts()).thenReturn(productDTOMap);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/sell-product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void productsController_whenThereIsNotEnoughStock_returnBadResponse() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Dining Chair");
        List<ProductDTO> products = objectMapper.readValue(new DefaultResourceLoader().getResource("files/product_list.json")
                .getInputStream(), new TypeReference<>() {});
        Map<String, ProductDTO> productDTOMap = products.stream().collect(Collectors.toMap(ProductDTO::getName, Function.identity()));
        when(productRepository.getAllProducts()).thenReturn(productDTOMap);
        when(articlesRepository.findAll()).thenReturn(Collections.emptyList());
        when(articlesRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/sell-product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void removeProduct_whenProductIsInCatalogue_returnsNoContent() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Dining Chair");
        List<ProductDTO> products = objectMapper.readValue(new DefaultResourceLoader().getResource("files/product_list.json")
                .getInputStream(), new TypeReference<>() {});

        when(productRepository.findByName(anyString())).thenReturn(products.stream().map(ProductMapper.MAPPER::toProductEntity)
                .filter(product -> "Dining chair".equalsIgnoreCase(product.getName())).toList());
        doNothing().when(productRepository).delete(any(Product.class));
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(delete("/remove-product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))).andExpect(status().isNoContent());
        verify(productRepository).delete(any(Product.class));
    }

    @Test
    void removeProduct_whenProductIsNotInCatalogue_returnsBadRequest() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Dining Chair");
        productDTO.setPrice(50);
        when(productRepository.findByName(anyString())).thenReturn(Collections.emptyList());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(delete("/remove-product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))).andExpect(status().isBadRequest());
        verify(productRepository, never()).delete(any(Product.class));
    }

}
