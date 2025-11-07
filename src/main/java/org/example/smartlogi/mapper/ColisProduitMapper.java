package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ColisProduitDTO;
import org.example.smartlogi.entity.ColisProduit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ColisProduitMapper {

    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    @Mapping(source = "produit.categorie", target = "produitCategorie")
    @Mapping(source = "produit.poids", target = "produitPoids")
    @Mapping(source = "produit.prix", target = "produitPrix")
    ColisProduitDTO toDTO(ColisProduit entity);

    List<ColisProduitDTO> toDTOList(List<ColisProduit> entities);
}