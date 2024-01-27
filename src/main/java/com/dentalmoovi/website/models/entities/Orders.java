package com.dentalmoovi.website.models.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import com.dentalmoovi.website.models.enums.StatusOrderList;

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
    private Set<ProductsRef> products = new HashSet<>();

    public void addProduct(Long idProudct, int amount){
        this.products.add(new ProductsRef(idProudct, amount));
    }

    public Set<Long> getProductsIds(){
        return this.products.stream()
                    .map(ProductsRef::getIdProduct)
                    .collect(Collectors.toSet());
    }

    public void deleteProduct(Long productId) {
        this.products.removeIf(productsRef -> productsRef.getIdProduct().equals(productId));
    }
}
