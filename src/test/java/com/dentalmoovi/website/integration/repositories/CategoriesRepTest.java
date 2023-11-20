package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.repositories.CategoriesRep;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
class CategoriesRepTest {
    
    @Autowired
    private CategoriesRep categoriesRep;

    private static Categories 
        parentCategoryOne, subCategoryOne1, subCategoryOne2;

    @BeforeAll
    static void setUp(@Autowired CategoriesRep categoriesRep){
        //parentCategories
        parentCategoryOne = Utils.setCategory("parentCategoryOne", null, categoriesRep);
        Utils.setCategory("parentCategoryTwo", null, categoriesRep);

        //subCategories
        subCategoryOne1 = Utils.setCategory("subCategoryOne1", parentCategoryOne.getId(), categoriesRep);
        subCategoryOne2 = Utils.setCategory("subCategoryOne2", parentCategoryOne.getId(), categoriesRep);

        //subSubCategory
        Utils.setCategory("subSubCategoryOne1", subCategoryOne1.getId(), categoriesRep);
    }

    @Test
    void parentCategoriesTest(){
        List<Categories> parentCategories = categoriesRep.findParentCategories();
        assertEquals( 2 , parentCategories.size());
    }

    @Test
    void subCategoriesTest(){
        List<Categories> subCategoriesLVL1 = categoriesRep.findByParentCategory(parentCategoryOne.getId());
        List<Categories> subCategoriesLVL2 = categoriesRep.findByParentCategory(subCategoryOne1.getId());
        assertEquals(2, subCategoriesLVL1.size());
        assertEquals(1, subCategoriesLVL2.size());
    }

    @Test
    void findByNameTest() throws Exception{
        Categories category = categoriesRep.findByName(subCategoryOne2.getName())
                                .orElseThrow(() -> new Exception("Category not found"));
        assertEquals(subCategoryOne2, category);
    }
}
