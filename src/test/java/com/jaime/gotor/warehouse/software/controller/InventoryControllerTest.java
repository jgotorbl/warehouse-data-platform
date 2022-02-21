package com.jaime.gotor.warehouse.software.controller;

import com.jaime.gotor.warehouse.software.WarehouseSoftwareApplication;
import com.jaime.gotor.warehouse.software.database.entity.Article;
import com.jaime.gotor.warehouse.software.database.repository.ArticlesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * InventoryControllerTest
 * <br>
 * <code>com.jaime.gotor.warehouse.software.controller.InventoryControllerTest</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 21 February 2022
 */
@SpringBootTest(classes = WarehouseSoftwareApplication.class)
public class InventoryControllerTest {
    
    @MockBean
    private ArticlesRepository articlesRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        List<Article> articles = new ArrayList<>();
        when(articlesRepository.saveAll(anyCollection())).thenReturn(articles);
    }

    @Test
    void inventoryController_whenValidFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/inventory.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile(
                "inventoryFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, fileResource.getInputStream().readAllBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-articles")
                        .file(inventoryFile)).andExpect(status().is(204));
        verify(articlesRepository).saveAll(anyCollection());
    }

    @Test
    void inventoryController_whenInvalidFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/inventory_invalid.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile(
                "inventoryFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, fileResource.getInputStream().readAllBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-articles")
                .file(inventoryFile)).andExpect(status().is(400));
        verify(articlesRepository, never()).saveAll(anyCollection());
    }

    @Test
    void inventoryController_whenEmptyFileIsSent_returnsNoContentAndIsSavedInDatabase() throws Exception {
        Resource fileResource = new ClassPathResource("files/inventory_invalid.json");
        assertNotNull(fileResource);

        MockMultipartFile inventoryFile = new MockMultipartFile(
                "inventoryFile", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, "".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-articles")
                .file(inventoryFile)).andExpect(status().is(400));
        verify(articlesRepository, never()).saveAll(anyCollection());
    }
}
