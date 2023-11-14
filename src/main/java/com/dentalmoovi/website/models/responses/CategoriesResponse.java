package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriesResponse {
    private List<CategoriesDTO> data;
}
