package com.apex.eqp.inventory;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.services.ProductService;
import com.apex.eqp.inventory.services.RecalledProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Autowired
    RecalledProductService recalledProductService;

    /**
     * Helper method to create test products
     */
    private Product createTestProduct(String productName, Double price, Integer quantity) {
        return Product.builder()
                .name(productName)
                .price(price)
                .quantity(quantity)
                .build();
    }

    /**
     * Helper method to create test recalled products
     */
    private RecalledProduct createTestRecalledProduct(String recalledProductName, Boolean expired) {
        return RecalledProduct.builder()
                .name(recalledProductName)
                .expired(expired)
                .build();
    }

    @Test
    public void shouldSaveProduct() {
        Product product = createTestProduct("product1", 1.2, 2);

        Product savedProduct = productService.save(product);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);

        Assertions.assertNotNull(loadedProduct);
        Assertions.assertNotNull(loadedProduct.getId());
    }

    @Test
    public void shouldUpdateProduct() {
        Product product = createTestProduct("product2", 1.3, 5);

        Product savedProduct = productService.save(product);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);

        Assertions.assertNotNull(loadedProduct);

        loadedProduct.setName("NewProduct");

        productService.save(loadedProduct);

        Assertions.assertNotNull(productService.findById(loadedProduct.getId()).orElse(null));
    }

    // Write your tests below
    @Test
    public void shouldFindSaveProduct() {
        Product product = createTestProduct("product3_new", 1.3, 5);
        Product savedProduct = productService.save(product);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);
        Assertions.assertNotNull(loadedProduct);

        List<Product> namedProducts = productService.findByName("product3_new");
        Assertions.assertNotNull(namedProducts);
    }

    @Test
    public void shouldFindPagedProducts() {
        Product product = createTestProduct("product3_paged", 1.3, 5);

        Product savedProduct = productService.save(product);
        Assertions.assertNotNull(savedProduct);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);
        Assertions.assertNotNull(loadedProduct);

        List<Product> namedProducts = productService.findByName("product3_paged");
        Assertions.assertNotNull(namedProducts);
        Assertions.assertNotNull(namedProducts.size() == 1);
        Assertions.assertNotNull(namedProducts.get(0) instanceof Product);

        Page<Product> pagedProduct = productService.findAll(Pageable.ofSize(10));
        Assertions.assertNotNull(pagedProduct);
        Assertions.assertNotNull(pagedProduct.getTotalElements() == 1);
        Assertions.assertNotNull(pagedProduct.getContent().get(0) instanceof Product);
        
    }
        
    @Test
    public void shouldProductIdMatch() {
        Product product = createTestProduct("product3_idmatch", 1.3, 5);
        Product savedProduct = productService.save(product);
        Assertions.assertNotNull(savedProduct);
        System.out.println("saveProductId=" + savedProduct.getId());

        List<Integer> idList = productService.findAllIdList(0, 1);
        Assertions.assertNotNull(idList);
        System.out.println("reirieved ids=" + idList);
        String msg = "idlist=" + idList + "-first id=" + idList.get(0) + "-savedId=" + savedProduct.getId();
        Assertions.assertTrue((idList.get(idList.size() - 1) == savedProduct.getId()), msg);
    }    
        
    @Test
    public void shouldUpdateTheProduct() {
        Product product = createTestProduct("productUpdate", 1.3, 5);

        Product savedProduct = productService.save(product);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);
        Assertions.assertNotNull(loadedProduct);

        loadedProduct.setName("NewProduct");
        productService.save(loadedProduct);
        Assertions.assertNotNull(productService.findById(loadedProduct.getId()).orElse(null));
        
        loadedProduct.setName("NewProductName");
        productService.updateProduct(loadedProduct, loadedProduct.getId());
        Assertions.assertNotNull(productService.findById(loadedProduct.getId()).orElse(null));
    }
    
    @Test
    public void displayProducts() {
        Product product = createTestProduct("productPaged", 1.3, 5);

        Product savedProduct = productService.save(product);

        Product loadedProduct = productService.findById(savedProduct.getId()).orElse(null);
        Assertions.assertNotNull(loadedProduct);

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by("name"));
        Page<Product> pagedProduct = productService.findAll(pageRequest);
        Assertions.assertNotNull(pagedProduct);
        Assertions.assertNotNull(pagedProduct.getTotalElements() > 0);
        Assertions.assertNotNull(pagedProduct.getContent().get(0) instanceof Product);
    }
}
