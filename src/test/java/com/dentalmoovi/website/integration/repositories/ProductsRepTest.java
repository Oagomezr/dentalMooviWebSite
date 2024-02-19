package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

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
        categoryX = categoriesRep.save(new Categories(null, "categoryX", null));
        categoryY = categoriesRep.save(new Categories(null, "categoryY", null));

        productX1 = productsRep.save(new Products(null, "productX1", "description productX1","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productX2", "description productX2","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productX3", "description productX3","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productX4", "description productX4","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productX5", "description productX5","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productX6", "description productX6","shortDescription", 1200, 4, true, null, categoryX.id()));
        productsRep.save(new Products(null, "productY1", "description productY1","shortDescription", 1200, 4, true, null, categoryY.id()));
        productsRep.save(new Products(null, "productY2", "description productY2","shortDescription", 1200, 4, true, null, categoryY.id()));
        productsRep.save(new Products(null, "productY3", "description productY3","shortDescription", 1200, 4, true, null, categoryY.id()));
        productsRep.save(new Products(null, "productY4", "description productY4","shortDescription", 1200, 4, true, null, categoryY.id()));
    }

    @Test
    void productsByCategoryNameTest(){
        List<Products> productsCategoryX = productsRep.findByCategoryName(categoryX.name());
        List<Products> productsCategoryY = productsRep.findByCategoryName(categoryY.name());
        assertEquals(6, productsCategoryX.size());
        assertEquals(4, productsCategoryY.size());
    }

    @Test
    void findByNameTest() throws Exception{
        Products product = productsRep.findByName(productX1.name())
                            .orElseThrow(() -> new Exception("Product not found"));
        assertEquals(product, productX1);
    }

    @Test
    void findByNameProductTest(){
        List<Products> productsFound = productsRep.findByNameContaining("prod", 9, 0);
        assertEquals(9, productsFound.size());
    }
}
