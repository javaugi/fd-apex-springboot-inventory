package com.apex.eqp.inventory.services;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.helpers.ProductFilter;
import com.apex.eqp.inventory.repositories.InventoryRepository;
import com.apex.eqp.inventory.repositories.ProductRepository;
import com.apex.eqp.inventory.repositories.RecalledProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryRepository inventoryRepository;
    private final RecalledProductRepository recalledProductRepository;
    private final ProductRepository productRepository;
    
    
    public Collection<Product> getAllUnfilteredProduct() {
        return inventoryRepository.findAll();
    }
    
    public void deleteAllProducts() {
        productRepository.deleteAllInBatch();
    }

    @Transactional
    public Product save(Product product) {
        return inventoryRepository.save(product);
    }

    @Transactional
    public RecalledProduct saveRecalled(RecalledProduct product) {
        return recalledProductRepository.save(product);
    }

    public Collection<Product> getAllNonRecalledProduct() {
        Collection<RecalledProduct> nonRecalled = recalledProductRepository.findAll();
        List<String> nonRecalledProductNames = nonRecalled.stream()
                .map(RecalledProduct::getName)
                .collect(Collectors.toList());
        System.out.println("getAllNonRecalledProduct size=" + nonRecalledProductNames.size());
        
        Collection<Product> allProducts = inventoryRepository.findAll();
        System.out.println("getAllProduct size=" + allProducts.size());        
        
        ProductFilter filter = new ProductFilter(new HashSet(nonRecalledProductNames));
        return filter.removeRecalledFrom(allProducts);
    }

    public Collection<Product> getAllNonRecalledProduct2() {
        Collection<RecalledProduct> nonRecalled = recalledProductRepository.findAll();
        List<String> nonRecalledProductNames = nonRecalled.stream()
                .map(RecalledProduct::getName)
                .collect(Collectors.toList());
        System.out.println("getAllNonRecalledProduct=" + nonRecalledProductNames);
        
        Collection<Product> allProducts = inventoryRepository.findAll();
        System.out.println("getAllProduct=" + allProducts);        
        
        ProductFilter filter = new ProductFilter(new HashSet(nonRecalledProductNames));
        return filter.doRemoveRecalledFrom(allProducts);
    }


    public Collection<Product> getAllProduct() {
        ProductFilter filter = new ProductFilter(null);

        return filter.removeRecalledFrom(inventoryRepository.findAll());
    }

    public Optional<Product> findById(Integer id) {
        return inventoryRepository.findById(id);
    }
    
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
    
    //*
    public Product updateProduct(Product product, Integer productId) {
        // Finds the existing department by ID.
        Product depDB = productRepository.findById(productId).get();
        
        // Updates fields if they are not null or empty.
        if (Objects.nonNull(product.getName()) && !"".equalsIgnoreCase(product.getName())) {
            depDB.setName(product.getName());
        }
        
        // Saves and returns the updated department entity.
        return productRepository.save(depDB);
    }
    // */
    
    
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    
    @Autowired
    private EntityManager entityManager;

    public List<Integer> findAllIdList(int offset, int limit) {
        @SuppressWarnings("unchecked")
        Query query = entityManager.createNativeQuery("select p.id from Product p");
        return query.getResultList();
    }
    
    
    public Iterable<Product> findSortedproducts() {
        return productRepository.findAll(Sort.by(
                        List.of( Sort.Order.desc("name"), Sort.Order.desc("price"))));
    }
    
}
