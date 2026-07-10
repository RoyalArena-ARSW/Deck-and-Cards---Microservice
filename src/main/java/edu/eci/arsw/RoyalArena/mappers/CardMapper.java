package edu.eci.arsw.RoyalArena.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.model.Building;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Spell;
import edu.eci.arsw.RoyalArena.model.Troop;

@Mapper(
    componentModel = "spring",
    uses = { TroopMapper.class, SpellMapper.class, BuildingMapper.class }
)
public interface CardMapper {

    /**
     * Convierte una carta cualquiera al DTO específico correspondiente.
     */
    default CardResponseDTO toDto(Card card, TroopMapper troopMapper,
                                   SpellMapper spellMapper, BuildingMapper buildingMapper) {
        if (card == null) return null;
        if (card instanceof Troop troop) return troopMapper.toDto(troop);
        if (card instanceof Spell spell) return spellMapper.toDto(spell);
        if (card instanceof Building building) return buildingMapper.toDto(building);
        throw new IllegalStateException("Unknown Card subtype: " + card.getClass().getName());
    }

    default List<CardResponseDTO> toDtoList(List<Card> cards, TroopMapper troopMapper,
                                             SpellMapper spellMapper, BuildingMapper buildingMapper) {
        if (cards == null) return List.of();
        return cards.stream()
                .map(c -> toDto(c, troopMapper, spellMapper, buildingMapper))
                .toList();
    }
}