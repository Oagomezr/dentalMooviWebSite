package com.dentalmoovi.website.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

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
        parentCategoryOne = categoriesRep.save(new Categories(null, "parentCategoryOne", null));
        categoriesRep.save(new Categories(null, "parentCategoryTwo", null));

        //subCategories
        subCategoryOne1 = categoriesRep.save(new Categories(null, "subCategoryOne1", parentCategoryOne.id()));
        subCategoryOne2 = categoriesRep.save(new Categories(null, "subCategoryOne2", parentCategoryOne.id()));

        subCategoryOne1 = categoriesRep.save(new Categories(null, "subCategoryOne1", parentCategoryOne.id()));
        subCategoryOne2 = categoriesRep.save(new Categories(null, "subCategoryOne2", parentCategoryOne.id()));

        //subSubCategory
        categoriesRep.save(new Categories( null, "subSubCategoryOne1", subCategoryOne1.id()));
    }

    @Test
    void parentCategoriesTest(){
        List<Categories> parentCategories = categoriesRep.findParentCategories();
        assertEquals( 2 , parentCategories.size());
    }

    @Test
    void subCategoriesTest(){
        List<Categories> subCategoriesLVL1 = categoriesRep.findByParentCategory(parentCategoryOne.id());
        List<Categories> subCategoriesLVL2 = categoriesRep.findByParentCategory(subCategoryOne1.id());
        assertEquals(2, subCategoriesLVL1.size());
        assertEquals(1, subCategoriesLVL2.size());
    }

    @Test
    void findByNameTest() throws Exception{
        Categories category = categoriesRep.findByName(subCategoryOne2.name())
                                .orElseThrow(() -> new Exception("Category not found"));
        assertEquals(subCategoryOne2, category);
    }
}
