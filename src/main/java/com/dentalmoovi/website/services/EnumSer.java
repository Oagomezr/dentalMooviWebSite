package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.Enum1DTO;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.responses.EnumResponse1;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;

@Service
public class EnumSer {
    private final DepartamentsRep dRep;
    private final MunicipalyRep mcRep;

    public EnumResponse1 getDepartamentsByContaining(String name){
        List<Departaments> departaments = dRep.findDepartamentByContaining(name);
        List<Enum1DTO> departamentsDTO = new ArrayList<>();
        departaments.stream().forEach(departament -> 
            departamentsDTO.add(new Enum1DTO(departament.id(), departament.name())));
        return new EnumResponse1(departamentsDTO);
    }

    public EnumResponse1 getMunicipalyByContaining(String name, int id){
        List<MunicipalyCity> municipalies = mcRep.findMunicipalyByContaining(name, id);
        List<Enum1DTO> municipalyDTO = new ArrayList<>();
        municipalies.stream().forEach(municipaly -> 
            municipalyDTO.add(new Enum1DTO(municipaly.id().intValue(), municipaly.name())));
        return new EnumResponse1(municipalyDTO);
    }

    public EnumSer(DepartamentsRep dRep, MunicipalyRep mcRep) {
        this.dRep = dRep;
        this.mcRep = mcRep;
    }
}
