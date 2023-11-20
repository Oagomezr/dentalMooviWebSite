package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Images {
    @Id
    private Long id;
    private String name;
    private String contentType;
    @EqualsAndHashCode.Include
    private byte[] data;
    private Long idProduct;
}
