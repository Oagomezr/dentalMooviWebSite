package com.dentalmoovi.website.integration.services;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.services.ProductsSer;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class ProductsSerIntegrationTest {

    @Autowired
    private ProductsSer productsSer;

    @Autowired
    private ProductsRep productsRep;

    @Autowired
    private CategoriesRep categoriesRep;

    @Test
    void getProductsByCategoryTest(){
        List<Categories> parentsCategories = categoriesRep.findParentCategories();
        assertEquals(6, parentsCategories.size());
        List<String> allCategories = new ArrayList<>();
        parentsCategories.stream().forEach(parentCategory ->{
            allCategories.addAll(getSubCategories(parentCategory));
            allCategories.add(parentCategory.name());
        });

        assertEquals(35, allCategories.size());

        int totalProductsExpected = allCategories.stream().mapToInt(category -> productsRep.findByCategoryName(category).size()).sum();
        int totalProductsResult = parentsCategories.stream().mapToInt(parentCategory -> productsSer.getProductsByCategory(parentCategory.name(),1,9,false).getTotalProducts()).sum();
        assertEquals(totalProductsExpected, totalProductsResult);
    }

    private List<String> getSubCategories(Categories parentCategory) {
        List<Categories> subCategories = categoriesRep.findByParentCategory(parentCategory.id());
        List<String> subcategoriesNames = new ArrayList<>();
        subCategories.stream().forEach(subCategory ->{
            subcategoriesNames.add(subCategory.name());
            subcategoriesNames.addAll(getSubCategories(subCategory));
        });
        return subcategoriesNames;
    }

    @Test
    void getProductsByContainingTest(){
        List<String> keyWords = List.of("a", "ar", "arcos", "b", "brackets", "ad", "distalizador");
        keyWords.stream().forEach(keyWord ->{
            List<Products> products7Found = productsRep.findByNameContaining(keyWord,7,0);
            List<Products> productsFound = productsRep.findByNameContaining(keyWord,9,0);
            ProductsResponse response7 = productsSer.getProductsByContaining(keyWord, true, 1, 9, false);
            ProductsResponse response = productsSer.getProductsByContaining(keyWord, false, 1, 9, false);
            assertEquals(products7Found.size(), response7.getTotalProducts());
            assertEquals(productsFound.size(), response.getTotalProducts());
            assertTrue(response.getPaginatedProducts() <= 9);
        });
        
    }

    @Test
    void getProductTest(){
        List<String> keyWords = List.of("Adhesivo1", "Alambre1", "bisacrilico1", "cemento1", "deltaForceBraket1", "estandarBraket1", "resina1");
        keyWords.stream().forEach(keyWord ->{
            Products product = productsRep.findByName(keyWord)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            ProductsDTO productResult = productsSer.getProduct(keyWord, false);
            assertEquals(product.name(), productResult.nameProduct());
            assertEquals(product.unitPrice(), productResult.unitPrice());
            assertEquals(product.description(), productResult.description());
            assertEquals(product.stock(), productResult.stock());
        });
    }
}
