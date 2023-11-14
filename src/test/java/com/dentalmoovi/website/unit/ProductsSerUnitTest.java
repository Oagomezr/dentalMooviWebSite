package com.dentalmoovi.website.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.services.ProductsSer;

@ExtendWith(MockitoExtension.class)
class ProductsSerUnitTest {
    @InjectMocks
    private ProductsSer productsSer;

    @Mock
    private ProductsRep productsRep;

    @Mock
    private CategoriesRep categoriesRep;

    private Categories parentCategoryX = Utils.setCategory("parent1", null);
    private Categories parentCategoryY = Utils.setCategory("parent2", null);
    private Categories subCategoryX1 = Utils.setCategory("subCategoryX1", parentCategoryX);
    private Categories subCategoryX2 = Utils.setCategory("subCategoryX2", parentCategoryX);
    private Categories subCategoryY1 = Utils.setCategory("subCategoryY1", parentCategoryY);
    private Categories subSubCategoryX2 = Utils.setCategory("subSubCategoryX2", subCategoryX2);

    private Products productosubX1one = Utils.setProduct("productosubX1one",  "description productosubX1one", 4, 1, subCategoryX1, true);
    private Products productosubX1two = Utils.setProduct("productosubX1two", "description productosubX1two", 4, 1, subCategoryX1, true);
    private Products productosubX1three = Utils.setProduct("productosubX1three", "description productosubX1three", 4, 1, subCategoryX1, true);
    private Products productosubX1four = Utils.setProduct("productosubX1four", "description productosubX1four", 4, 1, subCategoryX1, true);
    private Products productosubX1five = Utils.setProduct("productosubX1five", "description productosubX1five", 4, 1, subCategoryX1, true);
    private Products productosubX1six = Utils.setProduct("productosubX1six", "description productosubX1six", 4, 1, subCategoryX1, true);
    private Products productosubX2one = Utils.setProduct("productosubX2one", "description productosubX2one", 4, 1, subCategoryX2, true);
    private Products productosubX2two = Utils.setProduct("productosubX2two", "description productosubX2two", 4, 1, subCategoryX2, true);
    private Products productosubX2three = Utils.setProduct("productosubX2three",  "description productosubX2three", 4, 1, subCategoryX2, true);
    private Products productosubY1one = Utils.setProduct("productosubY1one", "description productosubY1one", 4, 1, subCategoryY1, true);
    private Products productosubY1two = Utils.setProduct("productosubY1two", "description productosubY1two", 4, 1, subCategoryY1, true);
    private Products productosubY1three = Utils.setProduct("productosubY1three",  "description productosubY1three", 4, 1, subCategoryY1, true);
    private Products productosubY1four = Utils.setProduct("productosubY1four", "description productosubY1four", 4, 1, subCategoryY1, true);
    private Products productosubSubX1one = Utils.setProduct("productosubSubX1one",  "description productosubSubX1one", 4, 1, subSubCategoryX2, true);
    private Products productosubSubX1two = Utils.setProduct("productosubSubX1two", "description productosubSubX1two", 4, 1, subSubCategoryX2, true);

    @Test
    void getProductsByCategoryTest(){
        when(categoriesRep.findByName(Mockito.any())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            switch (name) {
                case "parent1":
                    return Optional.of(parentCategoryX);
                case "parent2":
                    return Optional.of(parentCategoryY);
                case "subCategoryX1":
                    return Optional.of(subCategoryX1);
                case "subCategoryX2":
                    return Optional.of(subCategoryX2);
                case "subCategoryY1":
                    return Optional.of(subCategoryY1);
                case "subSubCategoryX2":
                    return Optional.of(subSubCategoryX2);
                default:
                    return Optional.empty();
            }
        });

        when(categoriesRep.findByParentCategoryOrderByName(Mockito.any())).thenAnswer(invocation -> {
            Categories parentCategory = invocation.getArgument(0);
            switch (parentCategory.getName()) {
                case "parent1":
                    return List.of(subCategoryX1, subCategoryX2);
                case "parent2":
                    return List.of(subCategoryY1);
                case "subCategoryX2":
                    return List.of(subSubCategoryX2);
                default:
                    return List.of();
            }
        });

        when(productsRep.findByCategoryName(Mockito.any())).thenAnswer(invocation -> {
            String categoryName = invocation.getArgument(0);
            switch (categoryName) {
                case "parent1":
                    return List.of();
                case "parent2":
                    return List.of();
                case "subCategoryX1":
                    return List.of(productosubX1one, productosubX1two, productosubX1three, productosubX1four, productosubX1five, productosubX1six);
                case "subCategoryX2":
                    return List.of(productosubX2one, productosubX2two, productosubX2three);
                case "subCategoryY1":
                    return List.of(productosubY1one, productosubY1two, productosubY1three, productosubY1four);
                case "subSubCategoryX2":
                    return List.of(productosubSubX1one, productosubSubX1two);
                default:
                    return null;
            }
        });

        ProductsResponse response1 = productsSer.getProductsByCategory("parent1", 1, 9);
        ProductsResponse response2 = productsSer.getProductsByCategory("parent2", 1, 9);

        assertEquals(11, response1.getTotalProducts());
        assertEquals(9, response1.getPaginatedProducts());
        assertEquals(4, response2.getTotalProducts());
        assertEquals(4, response2.getPaginatedProducts());
    }

    @Test
    void getProductTest(){
        when(productsRep.findByName(Mockito.any())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            switch (name) {
                case "productosubX1one":
                    return Optional.of(productosubX1one);
                case "productosubX2one":
                    return Optional.of(productosubX2one);
                case "productosubY1one":
                    return Optional.of(productosubY1one);
                case "productosubSubX1one":
                    return Optional.of(productosubSubX1one);
                default:
                    return Optional.empty();
            }
        });

        List<Products> productsTest = List.of(productosubX1one, productosubX2one, productosubY1one, productosubSubX1one);

        productsTest.stream().forEach(productTest ->{
            ProductsDTO expected = productsSer.getProduct(productTest.getName());
            assertEquals(expected.getNameProduct(), productTest.getName());
        });
    }

    @Test
    void getProductsByContainingTest(){
        when(productsRep.findTop7ByNameContainingIgnoreCase(Mockito.any())).thenAnswer(invocation -> {
            String categoryName = invocation.getArgument(0);
            switch (categoryName) {
                case "prod":
                    return List.of(productosubX1one, productosubX1two, productosubX1three, productosubX1four, productosubX1five, productosubX1six, productosubX2one);
                case "productosubX1":
                    return List.of(productosubX1one, productosubX1two, productosubX1three, productosubX1four, productosubX1five, productosubX1six);
                case "productosubX1t":
                    return List.of(productosubX1two, productosubX1three);
                case "productosubX1th":
                    return List.of(productosubX1three);
                default:
                    return null;
            }
        });

        when(productsRep.findByNameContainingIgnoreCase(Mockito.any())).thenAnswer(invocation -> {
            String categoryName = invocation.getArgument(0);
            switch (categoryName) {
                case "prod":
                    return List.of(productosubX1one, productosubX1two, productosubX1three, productosubX1four, 
                                    productosubX1five, productosubX1six, productosubX2one, productosubX2two,
                                    productosubX2three, productosubY1one, productosubY1two, productosubY1three,
                                    productosubY1four, productosubSubX1one, productosubSubX1two);
                case "productosubX1":
                    return List.of(productosubX1one, productosubX1two, productosubX1three, productosubX1four, productosubX1five, productosubX1six);
                case "productosubX1t":
                    return List.of(productosubX1two, productosubX1three);
                case "productosubX1th":
                    return List.of(productosubX1three);
                default:
                    return null;
            }
        });

        List<String> keyWordsForSearch = List.of("prod", "productosubX1", "productosubX1t", "productosubX1th");
        List<Integer> expected1 = List.of(7,6,2,1);
        List<Integer> expected2 = List.of(15,6,2,1);

        for(int i=0; i<keyWordsForSearch.size(); i++){
            int response1 = productsSer.getProductsByContaining(keyWordsForSearch.get(i), true, 1, 9).getTotalProducts();
            int response2 = productsSer.getProductsByContaining(keyWordsForSearch.get(i), false, 1, 9).getTotalProducts();
            assertEquals(expected1.get(i), response1);
            assertEquals(expected2.get(i), response2);
        }
    }
}
