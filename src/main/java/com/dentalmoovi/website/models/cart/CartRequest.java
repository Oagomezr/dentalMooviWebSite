package com.dentalmoovi.website.models.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private List<CartDtoRequest> data;
}
