package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ColisDTO;
import org.example.smartlogi.dto.ColisDetailDTO;
import org.example.smartlogi.entity.Colis;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ColisProduitMapper.class, HistoriqueLivraisonMapper.class}
)
public interface ColisMapper {

    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "zoneDestination.id", target = "zoneDestinationId")
    @Mapping(source = "zoneDestination.nom", target = "zoneDestinationNom")
    @Mapping(source = "livreurAssigne.id", target = "livreurAssigneId")
    @Mapping(source = "livreurAssigne.nom", target = "livreurAssigneNom")
    @Mapping(source = "statut", target = "statut")
    @Mapping(source = "priorite", target = "priorite")
    ColisDTO toDTO(Colis entity);

    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "clientExpediteur.prenom", target = "clientExpediteurPrenom")
    @Mapping(source = "clientExpediteur.email", target = "clientExpediteurEmail")
    @Mapping(source = "clientExpediteur.telephone", target = "clientExpediteurTelephone")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "destinataire.prenom", target = "destinatairePrenom")
    @Mapping(source = "destinataire.email", target = "destinataireEmail")
    @Mapping(source = "destinataire.telephone", target = "destinataireTelephone")
    @Mapping(source = "destinataire.adresse", target = "destinataireAdresse")
    @Mapping(source = "zoneDestination.id", target = "zoneDestinationId")
    @Mapping(source = "zoneDestination.nom", target = "zoneDestinationNom")
    @Mapping(source = "zoneDestination.ville", target = "zoneDestinationVille")
    @Mapping(source = "zoneDestination.codePostal", target = "zoneDestinationCodePostal")
    @Mapping(source = "livreurAssigne.id", target = "livreurAssigneId")
    @Mapping(source = "livreurAssigne.nom", target = "livreurAssigneNom")
    @Mapping(source = "livreurAssigne.prenom", target = "livreurAssignePrenom")
    @Mapping(source = "livreurAssigne.telephone", target = "livreurAssigneTelephone")
    @Mapping(source = "livreurAssigne.vehicule", target = "livreurAssigneVehicule")
    @Mapping(source = "statut", target = "statut")
    @Mapping(source = "priorite", target = "priorite")
    @Mapping(source = "colisProduits", target = "produits")
    @Mapping(source = "historiqueLivraisons", target = "historique")
    ColisDetailDTO toDetailDTO(Colis entity);

    List<ColisDTO> toDTOList(List<Colis> entities);

    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(source = "zoneDestinationId", target = "zoneDestination.id")
    @Mapping(source = "livreurAssigneId", target = "livreurAssigne.id")
    @Mapping(target = "clientExpediteur.nom", ignore = true)
    @Mapping(target = "clientExpediteur.prenom", ignore = true)
    @Mapping(target = "clientExpediteur.email", ignore = true)
    @Mapping(target = "clientExpediteur.telephone", ignore = true)
    @Mapping(target = "clientExpediteur.adresse", ignore = true)
    @Mapping(target = "clientExpediteur.createdAt", ignore = true)
    @Mapping(target = "clientExpediteur.updatedAt", ignore = true)
    @Mapping(target = "destinataire.nom", ignore = true)
    @Mapping(target = "destinataire.prenom", ignore = true)
    @Mapping(target = "destinataire.email", ignore = true)
    @Mapping(target = "destinataire.telephone", ignore = true)
    @Mapping(target = "destinataire.adresse", ignore = true)
    @Mapping(target = "destinataire.createdAt", ignore = true)
    @Mapping(target = "destinataire.updatedAt", ignore = true)
    @Mapping(target = "zoneDestination.nom", ignore = true)
    @Mapping(target = "zoneDestination.ville", ignore = true)
    @Mapping(target = "zoneDestination.codePostal", ignore = true)
    @Mapping(target = "zoneDestination.createdAt", ignore = true)
    @Mapping(target = "livreurAssigne.nom", ignore = true)
    @Mapping(target = "livreurAssigne.prenom", ignore = true)
    @Mapping(target = "livreurAssigne.telephone", ignore = true)
    @Mapping(target = "livreurAssigne.vehicule", ignore = true)
    @Mapping(target = "livreurAssigne.actif", ignore = true)
    @Mapping(target = "livreurAssigne.zoneAssignee", ignore = true)
    @Mapping(target = "livreurAssigne.createdAt", ignore = true)
    @Mapping(target = "colisProduits", ignore = true)
    @Mapping(target = "historiqueLivraisons", ignore = true)
    Colis toEntity(ColisDTO dto);

    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(source = "zoneDestinationId", target = "zoneDestination.id")
    @Mapping(source = "livreurAssigneId", target = "livreurAssigne.id")
    @Mapping(target = "clientExpediteur.nom", ignore = true)
    @Mapping(target = "clientExpediteur.prenom", ignore = true)
    @Mapping(target = "clientExpediteur.email", ignore = true)
    @Mapping(target = "clientExpediteur.telephone", ignore = true)
    @Mapping(target = "clientExpediteur.adresse", ignore = true)
    @Mapping(target = "clientExpediteur.createdAt", ignore = true)
    @Mapping(target = "clientExpediteur.updatedAt", ignore = true)
    @Mapping(target = "destinataire.nom", ignore = true)
    @Mapping(target = "destinataire.prenom", ignore = true)
    @Mapping(target = "destinataire.email", ignore = true)
    @Mapping(target = "destinataire.telephone", ignore = true)
    @Mapping(target = "destinataire.adresse", ignore = true)
    @Mapping(target = "destinataire.createdAt", ignore = true)
    @Mapping(target = "destinataire.updatedAt", ignore = true)
    @Mapping(target = "zoneDestination.nom", ignore = true)
    @Mapping(target = "zoneDestination.ville", ignore = true)
    @Mapping(target = "zoneDestination.codePostal", ignore = true)
    @Mapping(target = "zoneDestination.createdAt", ignore = true)
    @Mapping(target = "livreurAssigne.nom", ignore = true)
    @Mapping(target = "livreurAssigne.prenom", ignore = true)
    @Mapping(target = "livreurAssigne.telephone", ignore = true)
    @Mapping(target = "livreurAssigne.vehicule", ignore = true)
    @Mapping(target = "livreurAssigne.actif", ignore = true)
    @Mapping(target = "livreurAssigne.zoneAssignee", ignore = true)
    @Mapping(target = "livreurAssigne.createdAt", ignore = true)
    @Mapping(target = "colisProduits", ignore = true)
    @Mapping(target = "historiqueLivraisons", ignore = true)
    void updateEntityFromDTO(ColisDTO dto, @MappingTarget Colis entity);
}