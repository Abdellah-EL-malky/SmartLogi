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
    @Mapping(target = "livreurNom", expression = "java(entity.getColis() != null && entity.getColis().getLivreurAssigne() != null ? entity.getColis().getLivreurAssigne().getNom() + \" \" + entity.getColis().getLivreurAssigne().getPrenom() : null)")
    HistoriqueLivraisonDTO toDTO(HistoriqueLivraison entity);

    List<HistoriqueLivraisonDTO> toDTOList(List<HistoriqueLivraison> entities);

    @Mapping(source = "colisId", target = "colis.id")
    @Mapping(target = "colis.numeroSuivi", ignore = true)
    @Mapping(target = "colis.clientExpediteur", ignore = true)
    @Mapping(target = "colis.destinataire", ignore = true)
    @Mapping(target = "colis.zoneDestination", ignore = true)
    @Mapping(target = "colis.livreurAssigne", ignore = true)
    @Mapping(target = "colis.statut", ignore = true)
    @Mapping(target = "colis.priorite", ignore = true)
    @Mapping(target = "colis.poidsTotal", ignore = true)
    @Mapping(target = "colis.prixTotal", ignore = true)
    @Mapping(target = "colis.adresseLivraison", ignore = true)
    @Mapping(target = "colis.commentaire", ignore = true)
    @Mapping(target = "colis.dateLivraisonPrevue", ignore = true)
    @Mapping(target = "colis.dateLivraisonReelle", ignore = true)
    @Mapping(target = "colis.createdAt", ignore = true)
    @Mapping(target = "colis.updatedAt", ignore = true)
    @Mapping(target = "colis.colisProduits", ignore = true)
    @Mapping(target = "colis.historiqueLivraisons", ignore = true)
    HistoriqueLivraison toEntity(HistoriqueLivraisonDTO dto);
}