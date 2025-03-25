package com.apex.eqp.inventory.repositories;

import com.apex.eqp.inventory.entities.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p  WHERE p.name= (:name)")
    List<Product> findByName(@Param("name") String name);
    
    @Override
    public Page<Product> findAll(Pageable pageable);
    
    //Product updateProduct(Product product, Integer productId);
}
