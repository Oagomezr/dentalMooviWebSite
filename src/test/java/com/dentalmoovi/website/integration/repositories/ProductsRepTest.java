package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ProductsRep;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductsRepTest {
    
    @Autowired
    private ProductsRep productsRep;

    private static Categories categoryX = Utils.setCategory("categoryX", null);
    private static Categories categoryY = Utils.setCategory("categoryY", null);

    private static Products productX1 = Utils.setProduct("productX1", "description productX1", 1200, 4, categoryX, true);
    private static Products productX2 = Utils.setProduct("productX2", "description productX2", 1200, 4, categoryX, true);
    private static Products productX3 = Utils.setProduct("productX3", "description productX3", 1200, 4, categoryX, true);
    private static Products productX4 = Utils.setProduct("productX4", "description productX4", 1200, 4, categoryX, true);
    private static Products productX5 = Utils.setProduct("productX5", "description productX5", 1200, 4, categoryX, true);
    private static Products productX6 = Utils.setProduct("productX6", "description productX6", 1200, 4, categoryX, true);
    private static Products productY1 = Utils.setProduct("productY1", "description productY1", 1200, 4, categoryY, true);
    private static Products productY2 = Utils.setProduct("productY2", "description productY2", 1200, 4, categoryY, true);
    private static Products productY3 = Utils.setProduct("productY3", "description productY3", 1200, 4, categoryY, true);
    private static Products productY4 = Utils.setProduct("productY4", "description productY4", 1200, 4, categoryY, true);
    

    @TestConfiguration
    static class TestConfig {

        @Autowired
        private ProductsRep productsRep;

        @Autowired
        private CategoriesRep categoriesRep;

        private Set<Categories> categories = new HashSet<>(List.of(categoryX, categoryY));
        private Set<Products> products = new HashSet<>(List.of(productX1, productX2, productX3, productX4, 
                                            productX5, productX6, productY1, productY2, productY3, productY4));

        //Execute before all test
        @PostConstruct
        void init() {
            //Save data in database
            categoriesRep.saveAll(categories);
            productsRep.saveAll(products);
        }
    }

    @Test
    void productsByCategoryNameTest(){
        List<Products> productsCategoryX = productsRep.findByCategoryName(categoryX.getName());
        List<Products> productsCategoryY = productsRep.findByCategoryName(categoryY.getName());
        assertEquals(6, productsCategoryX.size());
        assertEquals(4, productsCategoryY.size());
    }

    @Test
    void findByNameTest() throws Exception{
        Products product = productsRep.findByName(productX1.getName())
                            .orElseThrow(() -> new Exception("Product not found"));
        assertEquals(product, productX1);
    }

    @Test
    void findByNameProductTest(){
        List<Products> productsFound = productsRep.findByNameContainingIgnoreCase("prod");
        assertEquals(10, productsFound.size());
    }

    @Test
    void findByNameWith7resultsTest(){
        List<Products> productsFound = productsRep.findTop7ByNameContainingIgnoreCase("prod");
        assertEquals(7, productsFound.size());
    }
}
