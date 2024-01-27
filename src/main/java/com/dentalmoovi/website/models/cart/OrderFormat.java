package com.dentalmoovi.website.models.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFormat {
    private Long orderNumber;
    private String customerName;
    private String customerLastName;
    private String celPhone;
    private String date;
    private String hour;
    private String departament;
    private String location;
    private String address;
    private String enterprise;
    private List<CartDtoRespose> products;
    private String total;
}
