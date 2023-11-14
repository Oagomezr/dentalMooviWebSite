package com.dentalmoovi.website.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImagesDTO {
    private String name;
    private String contentType;
    private String imageBase64;
}
