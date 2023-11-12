package com.dentalmoovi.website.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dentalmoovi.website.models.entities.Images;

public interface ImgRep extends JpaRepository<Images, Long>{
    
}
