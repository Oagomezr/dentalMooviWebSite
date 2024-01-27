package com.dentalmoovi.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.services.OrdersSer;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersSer ordersSer;

    @PostMapping("/user/generateOrder/{idAddress}")
    public void generateOrderByUser(@RequestBody CartRequest req, @PathVariable long idAddress, HttpServletResponse response) {
        ordersSer.downloadOrder(req, idAddress, false, response);
    }

    @PostMapping("/admin/generateOrder/{idAddress}")
    public void generateOrderByAdmin(@RequestBody CartRequest req, @PathVariable long idAddress, HttpServletResponse response) {
        ordersSer.downloadOrder(req, idAddress, true, response);
    }

}
