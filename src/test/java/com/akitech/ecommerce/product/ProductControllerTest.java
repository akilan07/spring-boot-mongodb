package com.akitech.ecommerce.product;

import com.akitech.ecommerce.common.InternalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldAddProductSuccessfully() throws Exception {
        Product product = new Product(1, "Shirt", 3, "Men Shirt Slim fit");

        mockMvc.perform(post("/products/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFailWhenServiceReturnFailed() throws Exception {
        Product product = new Product(1, "Shirt", 3, "Men Shirt Slim fit");
        Mockito.when(productService.addProduct(Mockito.any())).thenThrow(new InternalException());

        mockMvc.perform(post("/products/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Shirt", 3, "Men Shirt Slim fit"));
        products.add(new Product(2, "Pant", 3, "Men Pant Slim fit"));
        products.add(new Product(3, "TShirt", 3, "Men TShirt Slim fit"));
        Mockito.when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/products/"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetNOContent() throws Exception {
        List<Product> products = new ArrayList<>();
        Mockito.when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/products/"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldUpdateProductSuccessfully() throws Exception {
        Product product = new Product(1, "Shirt", 3, "Men Shirt Slim fit");

        mockMvc.perform(put("/products/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNoProductFound() throws Exception {
        Product product = new Product(1, "Shirt", 3, "Men Shirt Slim fit");
        Mockito.when(productService.updateProduct(Mockito.any())).thenThrow(new NoProductFound());

        mockMvc.perform(put("/products/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldSoftDeleteProductSuccessfully() throws Exception {
        int productID = 1;
        Product product = new Product(1, "Shirt", 3, "Men Shirt Slim fit");
        Mockito.when(productService.deleteProduct(Mockito.anyInt())).thenReturn(product);

        mockMvc.perform(delete("/products/"+productID))
                .andExpect(status().isOk());
    }
}