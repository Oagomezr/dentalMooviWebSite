package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ProductsRep;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
class ProductsRepTest {
    
    @Autowired
    private ProductsRep productsRep;

    private static Categories categoryX;
    private static Categories categoryY;

    private static Products productX1;
    
    @BeforeAll
    static void setUp(@Autowired CategoriesRep categoriesRep, @Autowired ProductsRep productsRep){
        categoryX = Utils.setCategory("categoryX", null, categoriesRep);
        categoryY = Utils.setCategory("categoryY", null, categoriesRep);

        productX1 = Utils.setProduct("productX1", "description productX1", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productX2", "description productX2", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productX3", "description productX3", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productX4", "description productX4", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productX5", "description productX5", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productX6", "description productX6", 1200, 4, categoryX.getId(), true, productsRep);
        Utils.setProduct("productY1", "description productY1", 1200, 4, categoryY.getId(), true, productsRep);
        Utils.setProduct("productY2", "description productY2", 1200, 4, categoryY.getId(), true, productsRep);
        Utils.setProduct("productY3", "description productY3", 1200, 4, categoryY.getId(), true, productsRep);
        Utils.setProduct("productY4", "description productY4", 1200, 4, categoryY.getId(), true, productsRep);
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
        List<Products> productsFound = productsRep.findByNameContaining("prod", 9, 0);
        assertEquals(9, productsFound.size());
    }
}
