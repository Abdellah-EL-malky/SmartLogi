package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.LivreurDTO;
import org.example.smartlogi.entity.Livreur;
import org.example.smartlogi.entity.Zone;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LivreurMapper {

    @Mapping(source = "zoneAssignee.id", target = "zoneAssigneeId")
    @Mapping(source = "zoneAssignee.nom", target = "zoneNom")
    @Mapping(source = "zoneAssignee.ville", target = "zoneVille")
    LivreurDTO toDTO(Livreur entity);

    @Mapping(source = "zoneAssigneeId", target = "zoneAssignee.id")
    @Mapping(target = "zoneAssignee.nom", ignore = true)
    @Mapping(target = "zoneAssignee.ville", ignore = true)
    @Mapping(target = "zoneAssignee.codePostal", ignore = true)
    @Mapping(target = "zoneAssignee.createdAt", ignore = true)
    Livreur toEntity(LivreurDTO dto);

    List<LivreurDTO> toDTOList(List<Livreur> entities);

    @Mapping(source = "zoneAssigneeId", target = "zoneAssignee.id")
    @Mapping(target = "zoneAssignee.nom", ignore = true)
    @Mapping(target = "zoneAssignee.ville", ignore = true)
    @Mapping(target = "zoneAssignee.codePostal", ignore = true)
    @Mapping(target = "zoneAssignee.createdAt", ignore = true)
    void updateEntityFromDTO(LivreurDTO dto, @MappingTarget Livreur entity);
}