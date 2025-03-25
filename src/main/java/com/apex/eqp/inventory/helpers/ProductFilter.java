package com.apex.eqp.inventory.helpers;

import com.apex.eqp.inventory.entities.Product;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductFilter {

    private final Set<String> recalledProducts;

    public ProductFilter(Set<String> recalledProducts) {
        this.recalledProducts = recalledProducts;
    }

    public List<Product> removeRecalledFrom(Collection<Product> allProduct) {
        //return allProduct.stream().filter(ProductFilter::filterByName).collect(Collectors.toList());
        return allProduct.stream()
            .filter(product -> (recalledProducts != null) && !recalledProducts.contains(product.getName()))
            .collect(Collectors.toList());
    }

    private static boolean filterByName(Product product) {
        return true;
    }
    

    private static boolean filterByName(Set<String> recalledProducts, Product product) {
        return (recalledProducts != null) && !recalledProducts.contains(product.getName());
    }
    
}
