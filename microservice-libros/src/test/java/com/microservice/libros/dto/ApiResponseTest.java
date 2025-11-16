package com.microservice.libros.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testApiResponse() {

        ApiResponse<String> response = new ApiResponse<>(
                true,
                200,
                "OK",
                "DATA",
                10L
        );

        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals("DATA", response.getData());
        assertEquals(10L, response.getSize());

        response.setSuccess(false);
        response.setStatus(404);
        response.setMessage("ERROR");
        response.setData("NUEVO");
        response.setSize(20L);

        assertFalse(response.isSuccess());
        assertEquals(404, response.getStatus());
        assertEquals("ERROR", response.getMessage());
        assertEquals("NUEVO", response.getData());
        assertEquals(20L, response.getSize());
    }
}
