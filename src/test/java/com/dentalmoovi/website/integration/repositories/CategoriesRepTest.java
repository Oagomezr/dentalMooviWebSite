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
import com.dentalmoovi.website.repositories.CategoriesRep;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CategoriesRepTest {
    
    @Autowired
    private CategoriesRep categoriesRep;

    //parentCategories
    private static Categories parentCategoryOne = Utils.setCategory("parentCategoryOne", null);
    private static Categories parentCategoryTwo = Utils.setCategory("parentCategoryTwo", null);

    //subCategories
    private static Categories subCategoryOne1 = Utils.setCategory("subCategoryOne1", parentCategoryOne);
    private static Categories subCategoryOne2 = Utils.setCategory("subCategoryOne2", parentCategoryOne);

    //subcategory's subcategories
    private static Categories subCategoryOne1One = Utils.setCategory("subCategoryOne1One", subCategoryOne1);

    //categories array
    private static Set<Categories> allCategories = new HashSet<>();

    @TestConfiguration
    static class TestConfig {

        @Autowired
        private CategoriesRep categoriesRep;

        //Execute before all test
        @PostConstruct
        void init() {
            //Save data in database
            allCategories.addAll(List.of(parentCategoryOne, parentCategoryTwo, subCategoryOne1, subCategoryOne2, subCategoryOne1One));
            categoriesRep.saveAll(allCategories);
        }
    }

    @Test
    void parentCategoriesTest(){
        List<Categories> parentCategories = categoriesRep.findByParentCategoryIsNullOrderByName();
        assertEquals( 2 , parentCategories.size());
    }

    @Test
    void subCategoriesTest(){
        List<Categories> subCategoriesLVL1 = categoriesRep.findByParentCategoryOrderByName(parentCategoryOne);
        List<Categories> subCategoriesLVL2 = categoriesRep.findByParentCategoryOrderByName(subCategoryOne1);
        assertEquals(2, subCategoriesLVL1.size());
        assertEquals(1, subCategoriesLVL2.size());
    }

    @Test
    void findByNameTest() throws Exception{
        Categories category = categoriesRep.findByName(subCategoryOne2.getName())
                                .orElseThrow(() -> new Exception("Category not found"));
        assertEquals(subCategoryOne2, category);
    }

    @Test
    void findMaxIdTest(){
        long idMax = categoriesRep.findMaxId();
        assertEquals(5, idMax);
    }

    @Test
    void countUpdates(){
        long countUpdates = categoriesRep.countUpdates();
        assertEquals(5, countUpdates);
    }
}
