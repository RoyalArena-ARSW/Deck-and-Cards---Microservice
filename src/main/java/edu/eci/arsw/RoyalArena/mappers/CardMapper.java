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
public abstract class CardMapper {

    protected TroopMapper troopMapper;
    protected SpellMapper spellMapper;
    protected BuildingMapper buildingMapper;

    public CardMapper(TroopMapper troopMapper, SpellMapper spellMapper, BuildingMapper buildingMapper) {
        this.troopMapper = troopMapper;
        this.spellMapper = spellMapper;
        this.buildingMapper = buildingMapper;
    }

    protected CardMapper() {}

    public CardResponseDTO toDto(Card card) {
        if (card == null) return null;
        if (card instanceof Troop troop) return troopMapper.toDto(troop);
        if (card instanceof Spell spell) return spellMapper.toDto(spell);
        if (card instanceof Building building) return buildingMapper.toDto(building);
        throw new IllegalStateException("Unknown Card subtype: " + card.getClass().getName());
    }

    public List<CardResponseDTO> toDtoList(List<Card> cards) {
        if (cards == null) return List.of();
        return cards.stream().map(this::toDto).toList();
    }
}