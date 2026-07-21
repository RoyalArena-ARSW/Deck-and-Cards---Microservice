package edu.eci.arsw.RoyalArena.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import edu.eci.arsw.RoyalArena.model.Troop;
import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.model.enums.Target;

/**
 * Verifica que Spring SEPA cablear el CardMapper polimórfico.
 *
 * Este es el test que atrapa el bug real: con inyección por constructor,
 * MapStruct llamaba al constructor vacío de la abstract class y los delegados
 * quedaban en null. Cambiar a @Autowired por campo lo arregló, porque Spring
 * instancia primero y llena los campos después por reflexión.
 *
 * Contexto mínimo (5 clases, sin BD ni web): arranca en milisegundos.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CardMapperImpl.class,
        DeckMapperImpl.class,
        TroopMapperImpl.class,
        SpellMapperImpl.class,
        BuildingMapperImpl.class
})
class MapperWiringTest {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private DeckMapper deckMapper;

    @Test
    @DisplayName("Spring inyecta los tres delegados en el CardMapper")
    void cardMapperDelegatesAreInjected() {
        assertThat(ReflectionTestUtils.getField(cardMapper, "troopMapper"))
                .as("si esto es null, volvio el bug de la inyeccion por constructor")
                .isNotNull();
        assertThat(ReflectionTestUtils.getField(cardMapper, "spellMapper")).isNotNull();
        assertThat(ReflectionTestUtils.getField(cardMapper, "buildingMapper")).isNotNull();
    }

    @Test
    @DisplayName("Spring inyecta los tres delegados en el DeckMapper")
    void deckMapperDelegatesAreInjected() {
        assertThat(ReflectionTestUtils.getField(deckMapper, "troopMapper")).isNotNull();
        assertThat(ReflectionTestUtils.getField(deckMapper, "spellMapper")).isNotNull();
        assertThat(ReflectionTestUtils.getField(deckMapper, "buildingMapper")).isNotNull();
    }

    /**
     * La prueba de fuego: el mapper cableado por Spring mapea sin NPE.
     * Con el bug original, esto reventaba con "troopMapper is null".
     */
    @Test
    @DisplayName("El mapper cableado por Spring mapea sin NullPointerException")
    void wiredMapperWorksEndToEnd() {
        Troop knight = Troop.builder()
                .id(1L).name("Knight").elixirCost(3)
                .rarity(Rarity.COMMON).level(11).unlockArena(0)
                .deploymentType(DeploymentType.OWN_SIDE)
                .damage(202).health(1766).isAerial(false)
                .attackSpeed(1.2).movementSpeed(MovementSpeed.MEDIUM)
                .attackRange(0.0).target(Target.GROUND).unitCount(1)
                .build();

        assertThat(cardMapper.toDto(knight)).isNotNull();
        assertThat(cardMapper.toDto(knight).getName()).isEqualTo("Knight");
    }
}