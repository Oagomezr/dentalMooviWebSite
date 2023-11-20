package com.dentalmoovi.website.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.services.CategoriesSer;


@ExtendWith(MockitoExtension.class)
class CategoriesSerUnitTest {
    
    @InjectMocks
    private CategoriesSer categoriesSer;

    @Mock
    private CategoriesRep categoriesRep;

    @Test
    void getAllCategoriesTest(){
        Categories parentCategoryX = new Categories();
        parentCategoryX.setId(1L);
        parentCategoryX.setName("parent1");
        Categories parentCategoryY = new Categories();
        parentCategoryY.setId(2L);
        parentCategoryY.setName("parent2");
        Categories subCategoryX1 = new Categories();
        subCategoryX1.setId(3L);
        subCategoryX1.setName("subCategoryX1");
        subCategoryX1.setIdParentCategory(parentCategoryX.getId());
        Categories subCategoryX2 = new Categories();
        subCategoryX2.setId(4L);
        subCategoryX2.setName("subCategoryX2");
        subCategoryX1.setIdParentCategory(parentCategoryX.getId());
        Categories subCategoryY1 = new Categories();
        subCategoryY1.setId(5L);
        subCategoryY1.setName("subCategoryY1");
        subCategoryX1.setIdParentCategory(parentCategoryY.getId());
        Categories subSubCategoryX2 = new Categories();
        subSubCategoryX2.setId(6L);
        subSubCategoryX2.setName("subSubCategoryX2");
        subCategoryX1.setIdParentCategory(subCategoryX2.getId());

        when(categoriesRep.findParentCategories()).thenReturn(List.of(parentCategoryX, parentCategoryY));
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

        CategoriesResponse response = categoriesSer.getAllCategories();
        CategoriesDTO result1 = response.getData().get(0).getChildrenCategories().get(1);
        List<String> expected1 = List.of("subCategoryX2","parent1");
        CategoriesDTO result2 = result1.getChildrenCategories().get(0);
        List<String> expected2 = List.of("subSubCategoryX2","subCategoryX2","parent1");
        //assertEquals(expected1, response.getData().get(0).getCategoryAndParents());
        assertEquals(expected1, result1.getCategoryAndParents());
        assertEquals(expected2, result2.getCategoryAndParents());
    }
}
