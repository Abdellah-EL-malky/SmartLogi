package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ColisDTO;
import org.example.smartlogi.dto.ColisDetailDTO;
import org.example.smartlogi.entity.Colis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ColisMapper {

    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "zone.id", target = "zoneDestinationId")
    @Mapping(source = "zone.nom", target = "zoneDestinationNom")
    @Mapping(source = "livreur.id", target = "livreurAssigneId")
    @Mapping(source = "livreur.nom", target = "livreurAssigneNom")
    @Mapping(source = "dateLivraisonPrevue", target = "dateLivraisonPrevue")
    @Mapping(source = "dateLivraisonEffective", target = "dateLivraisonReelle")
    @Mapping(source = "dateCreation", target = "createdAt")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "adresseLivraison", ignore = true)
    @Mapping(target = "commentaire", ignore = true)
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
    @Mapping(source = "zone.id", target = "zoneDestinationId")
    @Mapping(source = "zone.nom", target = "zoneDestinationNom")
    @Mapping(source = "zone.ville", target = "zoneDestinationVille")
    @Mapping(source = "zone.codePostal", target = "zoneDestinationCodePostal")
    @Mapping(source = "livreur.id", target = "livreurAssigneId")
    @Mapping(source = "livreur.nom", target = "livreurAssigneNom")
    @Mapping(source = "livreur.prenom", target = "livreurAssignePrenom")
    @Mapping(source = "livreur.telephone", target = "livreurAssigneTelephone")
    @Mapping(source = "livreur.vehicule", target = "livreurAssigneVehicule")
    @Mapping(source = "dateLivraisonPrevue", target = "dateLivraisonPrevue")
    @Mapping(source = "dateLivraisonEffective", target = "dateLivraisonReelle")
    @Mapping(source = "dateCreation", target = "createdAt")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "adresseLivraison", ignore = true)
    @Mapping(target = "commentaire", ignore = true)
    @Mapping(target = "produits", ignore = true)
    @Mapping(target = "historique", ignore = true)
    ColisDetailDTO toDetailDTO(Colis entity);

    List<ColisDTO> toDTOList(List<Colis> entities);
}