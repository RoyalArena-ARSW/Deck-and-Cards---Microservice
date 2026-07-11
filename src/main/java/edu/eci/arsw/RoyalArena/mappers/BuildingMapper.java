package edu.eci.arsw.RoyalArena.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.eci.arsw.RoyalArena.dto.request.BuildingRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.BuildingResponseDTO;
import edu.eci.arsw.RoyalArena.model.Building;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    @Mapping(target = "type", constant = "BUILDING")
    BuildingResponseDTO toDto(Building building);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Building toEntity(BuildingRequestDTO dto);
}