package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Addresses {
    @Id
    private Long id;
    private String departament;
    private String location;
    private String address;
    private String phone;
    private String description;
}
