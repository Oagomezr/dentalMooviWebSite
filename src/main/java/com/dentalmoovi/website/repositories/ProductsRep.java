package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Products;

public interface ProductsRep extends JpaRepository<Products, Long>{
    @Query("SELECT p FROM Products p WHERE p.category.name = :categoryName")
    List<Products> findByCategoryName(@Param("categoryName") String categoryName);

    Optional<Products> findByName(@Param("nameProduct") String nameProduct);
    List<Products> findByNameContainingIgnoreCase(String name);
    List<Products> findTop7ByNameContainingIgnoreCase(String name);
}
