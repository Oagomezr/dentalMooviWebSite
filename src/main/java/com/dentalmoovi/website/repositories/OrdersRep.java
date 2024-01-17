package com.dentalmoovi.website.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dentalmoovi.website.models.entities.Orders;

public interface OrdersRep extends CrudRepository<Orders, Long>{
    
}
