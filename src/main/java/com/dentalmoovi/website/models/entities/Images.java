package com.dentalmoovi.website.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;

    private String name;

    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @EqualsAndHashCode.Include
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Products product;
}
