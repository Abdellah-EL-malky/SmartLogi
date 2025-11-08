package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.HistoriqueLivraisonDTO;
import org.example.smartlogi.entity.HistoriqueLivraison;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface HistoriqueLivraisonMapper {

    @Mapping(source = "colis.id", target = "colisId")
    @Mapping(source = "statut", target = "statut")
    @Mapping(target = "livreurNom", ignore = true)
    @Mapping(target = "localisation", ignore = true)
    HistoriqueLivraisonDTO toDTO(HistoriqueLivraison entity);

    List<HistoriqueLivraisonDTO> toDTOList(List<HistoriqueLivraison> entities);
}