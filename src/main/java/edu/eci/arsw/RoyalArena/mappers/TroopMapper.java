package edu.eci.arsw.RoyalArena.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.eci.arsw.RoyalArena.dto.request.TroopRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.TroopResponseDTO;
import edu.eci.arsw.RoyalArena.model.Troop;

@Mapper(componentModel = "spring")
public interface TroopMapper {

    @Mapping(target = "type", constant = "TROOP")
    TroopResponseDTO toDto(Troop troop);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Troop toEntity(TroopRequestDTO dto);
}





