package com.dentalmoovi.website.models.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDtoRequest {
    private long id;
    private double prize;
    private int amount;
}
