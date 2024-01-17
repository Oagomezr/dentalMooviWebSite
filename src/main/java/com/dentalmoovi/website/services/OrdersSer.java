package com.dentalmoovi.website.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.enums.StatusOrderList;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.UserRep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersSer {
    private final OrdersRep ordersRep;
    private final UserRep userRep;

    public MessageDTO generateOrder(CartRequest req, long idAddress){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        Users user= this.userRep.findByEmail(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Utils.setOrder(StatusOrderList.PENDING, "file", user.getId(), idAddress, req, ordersRep);

        return new MessageDTO("Order Created");
    }
}
