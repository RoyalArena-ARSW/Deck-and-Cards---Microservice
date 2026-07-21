package edu.eci.arsw.RoyalArena.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils; // Importante para inyectar los mocks

import edu.eci.arsw.RoyalArena.dto.response.DeckResponseDTO;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.mappers.*; // Importa tus mappers
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Deck;
import edu.eci.arsw.RoyalArena.model.Troop;
import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.model.enums.Target;
import edu.eci.arsw.RoyalArena.repository.DeckRepository;

class DeckServiceTest {

    private final DeckRepository deckRepository = mock(DeckRepository.class);

    // 1. Mocks necesarios para el Mapper
    private final TroopMapper troopMapper = mock(TroopMapper.class);
    private final SpellMapper spellMapper = mock(SpellMapper.class);
    private final BuildingMapper buildingMapper = mock(BuildingMapper.class);

    private Deck activeDeckOf(Long userId) {
        List<Card> cards = new ArrayList<>();
        for (long i = 1; i <= 8; i++) {
            cards.add(Troop.builder()
                    .id(i).name("Card" + i).elixirCost(3)
                    .rarity(Rarity.COMMON).level(11).unlockArena(0)
                    .deploymentType(DeploymentType.OWN_SIDE)
                    .damage(100).health(500).isAerial(false)
                    .attackSpeed(1.0).movementSpeed(MovementSpeed.MEDIUM)
                    .attackRange(1.0).target(Target.GROUND).unitCount(1)
                    .build());
        }
        return Deck.builder()
                .id(1L).name("Ciclo Rapido").userId(userId).isActive(true).cards(cards)
                .build();
    }

    @Test
    @DisplayName("Devuelve el mazo activo de cualquier usuario con sus 8 cartas")
    void returnsActiveDeckWithAllCards() {
        DeckService service = newService();
        when(deckRepository.findByUserIdAndIsActiveTrue(1L))
                .thenReturn(Optional.of(activeDeckOf(1L)));

        DeckResponseDTO dto = service.getActiveDeckByUserId(1L);

        assertThat(dto.getName()).isEqualTo("Ciclo Rapido");
        assertThat(dto.getCards()).hasSize(8);
        assertThat(dto.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Sin mazo activo lanza 404")
    void throwsWhenNoActiveDeck() {
        DeckService service = newService();
        when(deckRepository.findByUserIdAndIsActiveTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getActiveDeckByUserId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Busca por userId Y activo")
    void queriesByUserIdAndActiveFlag() {
        DeckService service = newService();
        when(deckRepository.findByUserIdAndIsActiveTrue(1L))
                .thenReturn(Optional.of(activeDeckOf(1L)));

        service.getActiveDeckByUserId(1L);

        verify(deckRepository).findByUserIdAndIsActiveTrue(1L);
    }

    private DeckService newService() {
        // 2. Instanciamos el mapper
        DeckMapperImpl mapper = new DeckMapperImpl();
        
        // 3. Inyectamos los mocks manualmente usando ReflectionTestUtils 
        // (esto evita el NullPointerException al no tener el contexto de Spring completo)
        ReflectionTestUtils.setField(mapper, "troopMapper", troopMapper);
        ReflectionTestUtils.setField(mapper, "spellMapper", spellMapper);
        ReflectionTestUtils.setField(mapper, "buildingMapper", buildingMapper);

        // 4. Pasamos el mapper ya configurado al servicio
        return new DeckService(deckRepository, 
                mock(edu.eci.arsw.RoyalArena.repository.CardRepository.class),
                mapper);
    }
}