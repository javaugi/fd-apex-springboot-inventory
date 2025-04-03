package com.apex.eqp.inventory.controllers;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
//@RequestMapping(value = "api/inventory/product")
public class ProductUnfilteredController {

    private final ProductService productService;
    
    // to display all products at localhost:8080
    // to see database values at localhost:8080/h2-console
    @GetMapping
    public ResponseEntity<Collection<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllUnfilteredProduct());
    }
}
