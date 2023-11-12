package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Categories;

public interface CategoriesRep extends JpaRepository<Categories, Long>{
    List<Categories> findByParentCategoryIsNullOrderByName();
    List<Categories> findByParentCategoryOrderByName(@Param("parent_category") Categories parentCategory);
    Optional<Categories> findByName(@Param("name") String name);
    
    @Query("SELECT MAX(e.id) FROM Categories e")
    long findMaxId();

    @Query("SELECT SUM(e.numberUpdates) FROM Categories e")
    long countUpdates();
}
