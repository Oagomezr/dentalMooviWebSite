package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.AddressesDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressesResponse {
    private List<AddressesDTO> idsAddresses;
}
