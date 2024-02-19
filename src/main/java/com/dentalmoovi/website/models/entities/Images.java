package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

public record Images(
    @Id Long id,
    String name,
    String contentType,
    byte[] data,
    Long idProduct
) {}
