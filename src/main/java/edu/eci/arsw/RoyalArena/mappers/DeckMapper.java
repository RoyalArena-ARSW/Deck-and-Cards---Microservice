package edu.eci.arsw.RoyalArena.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.dto.response.DeckResponseDTO;
import edu.eci.arsw.RoyalArena.model.Building;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Deck;
import edu.eci.arsw.RoyalArena.model.Spell;
import edu.eci.arsw.RoyalArena.model.Troop;

@Mapper(
    componentModel = "spring",
    uses = { TroopMapper.class, SpellMapper.class, BuildingMapper.class }
)
public abstract class DeckMapper {

    protected TroopMapper troopMapper;
    protected SpellMapper spellMapper;
    protected BuildingMapper buildingMapper;

    /**
     * Inyección de los mappers hijos que necesitamos usar en mapCards.
     * MapStruct los inyecta automáticamente porque están declarados en 'uses'.
     */
    public DeckMapper(TroopMapper troopMapper, SpellMapper spellMapper, BuildingMapper buildingMapper) {
        this.troopMapper = troopMapper;
        this.spellMapper = spellMapper;
        this.buildingMapper = buildingMapper;
    }

    // Constructor por defecto que MapStruct necesita
    protected DeckMapper() {}

    @Mapping(target = "cards", source = "cards")
    @Mapping(target = "averageElixirCost", expression = "java(deck.getAverageElixirCost())")
    public abstract DeckResponseDTO toDto(Deck deck);

    /**
     * Método que MapStruct usa automáticamente para mapear la lista de cartas.
     * Cuando ve que hay que convertir List<Card> a List<CardResponseDTO>,
     * llama a este método para cada elemento.
     */
    protected CardResponseDTO cardToDto(Card card) {
        if (card == null) return null;
        if (card instanceof Troop troop) return troopMapper.toDto(troop);
        if (card instanceof Spell spell) return spellMapper.toDto(spell);
        if (card instanceof Building building) return buildingMapper.toDto(building);
        throw new IllegalStateException("Unknown Card subtype: " + card.getClass().getName());
    }
}