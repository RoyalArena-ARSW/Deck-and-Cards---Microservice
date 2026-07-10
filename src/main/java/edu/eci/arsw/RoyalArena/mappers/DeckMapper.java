package edu.eci.arsw.RoyalArena.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.dto.response.DeckResponseDTO;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Deck;

@Mapper(
    componentModel = "spring",
    uses = { CardMapper.class, TroopMapper.class, SpellMapper.class, BuildingMapper.class }
)
public interface DeckMapper {

    @Mapping(target = "cards", source = "cards", qualifiedByName = "mapCardsPolymorphic")
    @Mapping(target = "averageElixirCost", expression = "java(deck.getAverageElixirCost())")
    DeckResponseDTO toDto(Deck deck);

    @Named("mapCardsPolymorphic")
    default List<CardResponseDTO> mapCards(
            List<Card> cards,
            CardMapper cardMapper,
            TroopMapper troopMapper,
            SpellMapper spellMapper,
            BuildingMapper buildingMapper) {
        return cardMapper.toDtoList(cards, troopMapper, spellMapper, buildingMapper);
    }
}