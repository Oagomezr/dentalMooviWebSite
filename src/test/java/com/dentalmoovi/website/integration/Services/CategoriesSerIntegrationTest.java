package com.dentalmoovi.website.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.services.CategoriesSer;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class CategoriesSerIntegrationTest {

    @Autowired
    private CategoriesSer categoriesSer;

    @Autowired
    private CategoriesRep categoriesRep;

    @Test
    void getAllCategoriesTest(){
        //Test parent categories first
        CategoriesResponse response = categoriesSer.getAllCategories();
        List<CategoriesDTO> resultParents = response.getData();
        List<Categories> expectedParents = categoriesRep.findParentCategories();
        assertEquals(expectedParents.size(), resultParents.size());
        //Test subcategories
        testSubCategories(resultParents);
    }

    private void testSubCategories(List<CategoriesDTO> parents){
        if(parents.size() == 0) return;
        parents.stream().forEach(parentCategoryDTO ->{
            String nameParentCategory = parentCategoryDTO.getCategoryAndParents().get(0);
            Categories parentCategory = categoriesRep.findByName(nameParentCategory)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
            List<Categories> expected = categoriesRep.findByParentCategory(parentCategory.getId());
            List<CategoriesDTO> result = parentCategoryDTO.getChildrenCategories();
            assertEquals(expected.size(), result.size());
            //Test sub-Subcategories
            testSubCategories(result);
        });
    }
}
