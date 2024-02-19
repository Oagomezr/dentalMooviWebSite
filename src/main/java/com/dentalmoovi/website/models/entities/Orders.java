package com.dentalmoovi.website.models.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.models.entities.many_to_many.OrdersProducts;

import lombok.Data;

@Data
public class Orders {
    @Id
    private Long id;
    private byte[] orderFile;
    private StatusOrderList status;
    private Long idUser;
    private Long idAddress;

    @MappedCollection(idColumn = "id_order")
    private Set<OrdersProducts> products = new HashSet<>();

    public void addProduct(Long idProduct, int amount){
        this.products.add(new OrdersProducts(id ,idProduct, amount));
    }

    public Set<Long> getProductsIds(){
        return this.products.stream()
                    .map(OrdersProducts::idProduct)
                    .collect(Collectors.toSet());
    }

    public void deleteProduct(Long productId) {
        this.products.removeIf(productsRef -> productsRef.idProduct().equals(productId));
    }
}
