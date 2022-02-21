package com.jaime.gotor.warehouse.software.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * FileReaderService
 * <br>
 * <code>com.jaime.gotor.warehouse.software.service.FileReaderService</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseFileReaderService<T> {

    private final ObjectMapper objectMapper;

    /**
     * Reads the content of a file and stores it in a bean which is passed as argument
     *
     * @param content the file content in a byte array
     * @param tClass bean type that we want to store the file data into.
     * @return a bean containing the object data
     * @throws IOException if the file data is corrupted
     */
    public T readFileContent(byte[] content, Class<T> tClass) throws IOException {
        return objectMapper.readValue(content, tClass);
    }

}
