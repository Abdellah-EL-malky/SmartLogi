package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.DestinataireDTO;
import org.example.smartlogi.entity.Destinataire;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DestinataireMapper {

    DestinataireDTO toDTO(Destinataire entity);

    Destinataire toEntity(DestinataireDTO dto);

    List<DestinataireDTO> toDTOList(List<Destinataire> entities);

    void updateEntityFromDTO(DestinataireDTO dto, @MappingTarget Destinataire entity);
}