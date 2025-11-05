package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.entity.ClientExpediteur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientExpediteurMapper {

    ClientExpediteurDTO toDTO(ClientExpediteur entity);

    ClientExpediteur toEntity(ClientExpediteurDTO dto);

    List<ClientExpediteurDTO> toDTOList(List<ClientExpediteur> entities);

    void updateEntityFromDTO(ClientExpediteurDTO dto, @MappingTarget ClientExpediteur entity);
}