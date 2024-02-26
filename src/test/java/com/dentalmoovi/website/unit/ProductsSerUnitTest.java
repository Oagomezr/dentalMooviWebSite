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

    private static Categories parentCategoryX = setCategory(1L, "parent1", null);
    private static Categories parentCategoryY = setCategory(2L, "parent2", null);
    private static Categories subCategoryX1 = setCategory(3L, "subCategoryX1", parentCategoryX.id());
    private static Categories subCategoryX2 = setCategory(4L, "subCategoryX2", parentCategoryX.id());
    private static Categories subCategoryY1 = setCategory(5L, "subCategoryY1", parentCategoryY.id());
    private static Categories subSubCategoryX2 = setCategory(6L, "subSubCategoryX2", subCategoryX2.id());

    private static Products productosubX1one = setProduct(1L,"productosubX1one",  "description productosubX1one", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX1two = setProduct(2L,"productosubX1two", "description productosubX1two", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX1three = setProduct(3L,"productosubX1three", "description productosubX1three", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX1four = setProduct(4L,"productosubX1four", "description productosubX1four", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX1five = setProduct(5L,"productosubX1five", "description productosubX1five", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX1six = setProduct(6L,"productosubX1six", "description productosubX1six", 4, 1, subCategoryX1.id(), true);
    private static Products productosubX2one = setProduct(7L,"productosubX2one", "description productosubX2one", 4, 1, subCategoryX2.id(), true);
    private static Products productosubX2two = setProduct(8L,"productosubX2two", "description productosubX2two", 4, 1, subCategoryX2.id(), true);
    private static Products productosubX2three = setProduct(9L,"productosubX2three",  "description productosubX2three", 4, 1, subCategoryX2.id(), true);
    private static Products productosubY1one = setProduct(10L,"productosubY1one", "description productosubY1one", 4, 1, subCategoryY1.id(), true);
    private static Products productosubY1two = setProduct(11L,"productosubY1two", "description productosubY1two", 4, 1, subCategoryY1.id(), true);
    private static Products productosubY1three = setProduct(12L,"productosubY1three",  "description productosubY1three", 4, 1, subCategoryY1.id(), true);
    private static Products productosubY1four = setProduct(13L,"productosubY1four", "description productosubY1four", 4, 1, subCategoryY1.id(), true);
    private static Products productosubSubX1one = setProduct(14L,"productosubSubX1one",  "description productosubSubX1one", 4, 1, subSubCategoryX2.id(), true);
    private static Products productosubSubX1two = setProduct(15L,"productosubSubX1two", "description productosubSubX1two", 4, 1, subSubCategoryX2.id(), true);

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

        when(categoriesRep.findByParentCategory(Mockito.any())).thenAnswer(invocation -> {
            Long idCategory = invocation.getArgument(0);
            int intIdCategory = idCategory.intValue();
            switch (intIdCategory) {
                case 1:
                    return List.of(subCategoryX1, subCategoryX2);
                case 2:
                    return List.of(subCategoryY1);
                case 4:
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

        ProductsResponse response1 = productsSer.getProductsByCategory("parent1", 1, 9, false);
        ProductsResponse response2 = productsSer.getProductsByCategory("parent2", 1, 9, false);

        assertEquals(11, response1.getTotalProducts());
        assertEquals(9, response1.getPaginatedProducts());
        assertEquals(4, response2.getTotalProducts());
        assertEquals(4, response2.getPaginatedProducts());
    }

    @SuppressWarnings("null")
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

        when(categoriesRep.findById(Mockito.any())).thenAnswer(invocation -> {
            Long idCategory = invocation.getArgument(0);
            int intIdCategory = idCategory.intValue();
            switch (intIdCategory) {
                case 1:
                    return Optional.of(parentCategoryX);
                case 2:
                    return Optional.of(parentCategoryY);
                case 3:
                    return Optional.of(subCategoryX1);
                case 4:
                    return Optional.of(subCategoryX2);
                case 5:
                    return Optional.of(subCategoryY1);
                case 6:
                    return Optional.of(subSubCategoryX2);
                default:
                    return null;
            }
        });

        List<Products> productsTest = List.of(productosubX1one, productosubX2one, productosubY1one, productosubSubX1one);

        productsTest.stream().forEach(productTest ->{
            ProductsDTO expected = productsSer.getProduct(productTest.name(),false);
            assertEquals(expected.nameProduct(), productTest.name());
        });
    }

    @Test
    void getProductsByContainingTest(){
        when(productsRep.findByNameContaining(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenAnswer(invocation -> {
            if (!invocation.getArgument(2).equals(0)) return null;
            String categoryName = invocation.getArgument(0);
            int limit = invocation.getArgument(1);
            switch (limit) {
                case 7:
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
                case 9:
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
                default:
                    return null;
            }
        });

        List<String> keyWordsForSearch = List.of("prod", "productosubX1", "productosubX1t", "productosubX1th");
        List<Integer> expected1 = List.of(7,6,2,1);
        List<Integer> expected2 = List.of(15,6,2,1);

        for(int i=0; i<keyWordsForSearch.size(); i++){
            int response1 = productsSer.getProductsByContaining(keyWordsForSearch.get(i), true, 1, 7, false).getTotalProducts();
            int response2 = productsSer.getProductsByContaining(keyWordsForSearch.get(i), false, 1, 9, false).getTotalProducts();
            assertEquals(expected1.get(i), response1);
            assertEquals(expected2.get(i), response2);
        }
    }

    private static Categories setCategory(Long id, String name, Long idparentCategory){
        Categories category = new Categories(id, name, idparentCategory);
        return category;
    }

    private static Products setProduct(Long id, String name, String description, double unitprice, int stock, Long idCategory, boolean openToPublic){
        
        return new Products(id, name, description, null, unitprice, stock, openToPublic, null, idCategory);
    }
}
