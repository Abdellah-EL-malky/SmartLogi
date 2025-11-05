package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ProduitDTO;
import org.example.smartlogi.entity.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProduitMapper {

    ProduitDTO toDTO(Produit entity);

    Produit toEntity(ProduitDTO dto);

    List<ProduitDTO> toDTOList(List<Produit> entities);

    void updateEntityFromDTO(ProduitDTO dto, @MappingTarget Produit entity);
}