package edu.eci.arsw.RoyalArena.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.eci.arsw.RoyalArena.dto.request.SpellRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.SpellResponseDTO;
import edu.eci.arsw.RoyalArena.model.Spell;

@Mapper(componentModel = "spring")
public interface SpellMapper {

    @Mapping(target = "type", constant = "SPELL")
    SpellResponseDTO toDto(Spell spell);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Spell toEntity(SpellRequestDTO dto);
}