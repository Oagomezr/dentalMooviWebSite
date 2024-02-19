package com.dentalmoovi.website.repositories.enums;

import org.springframework.data.repository.CrudRepository;

import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;

public interface MunicipalyRep extends CrudRepository<MunicipalyCity, Integer>{
    
}
