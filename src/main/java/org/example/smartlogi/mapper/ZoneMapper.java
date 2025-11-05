package org.example.smartlogi.mapper;

import org.example.smartlogi.dto.ZoneDTO;
import org.example.smartlogi.entity.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ZoneMapper {

    ZoneDTO toDTO(Zone entity);

    Zone toEntity(ZoneDTO dto);

    List<ZoneDTO> toDTOList(List<Zone> entities);

    void updateEntityFromDTO(ZoneDTO dto, @MappingTarget Zone entity);
}