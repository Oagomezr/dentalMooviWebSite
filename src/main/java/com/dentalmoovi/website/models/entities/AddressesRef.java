package com.dentalmoovi.website.models.entities;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table("users_addresses")
@Data
@AllArgsConstructor
public class AddressesRef {
    private Long idAddress;
}
