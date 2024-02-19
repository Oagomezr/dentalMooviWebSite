package com.dentalmoovi.website.repositories.enums;

import org.springframework.data.repository.CrudRepository;

import com.dentalmoovi.website.models.entities.enums.Departaments;

public interface DepartamentsRep extends CrudRepository<Departaments, Integer>{
    
}
