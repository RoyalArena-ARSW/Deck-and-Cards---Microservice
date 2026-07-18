package edu.eci.arsw.RoyalArena.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.model.Building;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Spell;
import edu.eci.arsw.RoyalArena.model.Troop;
import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.model.enums.Target;

/**
 * El mapper polimórfico: recibe una Card abstracta y despacha al mapper del
 * subtipo concreto (Troop/Spell/Building).
 *
 * Este es EL caso raro del proyecto. Los demás mappers son interfaces simples;
 * este es una abstract class con inyección por campo, y esa combinación nos
 * costó horas de NullPointerException ("troopMapper is null") hasta entender
 * que MapStruct llamaba al constructor vacío y dejaba los campos sin inyectar.
 *
 * Aquí inyectamos los delegados a mano (Spring no está en un test unitario)
 * para probar la LÓGICA de despacho. El test de wiring de más abajo verifica
 * que Spring sepa cablearlo, que es lo que realmente se había roto.
 */
class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapperImpl();
        // Los delegados son interfaces simples: se instancian solos
        ReflectionTestUtils.setField(cardMapper, "troopMapper", new TroopMapperImpl());
        ReflectionTestUtils.setField(cardMapper, "spellMapper", new SpellMapperImpl());
        ReflectionTestUtils.setField(cardMapper, "buildingMapper", new BuildingMapperImpl());
    }

    // ===== Fixtures =====

    private Troop knight() {
        return Troop.builder()
                .id(1L).name("Knight").description("A tough melee fighter")
                .elixirCost(3).rarity(Rarity.COMMON).level(11).unlockArena(0)
                .deploymentType(DeploymentType.OWN_SIDE)
                .damage(202).health(1766).isAerial(false)
                .attackSpeed(1.2).movementSpeed(MovementSpeed.MEDIUM)
                .attackRange(0.0).target(Target.GROUND).unitCount(1)
                .build();
    }

    private Spell fireball() {
        return Spell.builder()
                .id(15L).name("Fireball").description("Bola de fuego")
                .elixirCost(4).rarity(Rarity.RARE).level(11).unlockArena(0)
                .deploymentType(DeploymentType.ANYWHERE)
                .damage(689).effectRadius(2.5).duration(0.0)
                .build();
    }

    private Building cannon() {
        return Building.builder()
                .id(16L).name("Cannon").description("Torre defensiva")
                .elixirCost(3).rarity(Rarity.COMMON).level(11).unlockArena(3)
                .deploymentType(DeploymentType.OWN_SIDE)
                .damage(212).health(742)
                .attackSpeed(0.8).attackRange(5.5).target(Target.GROUND)
                .lifetimeSeconds(30)
                .build();
    }

    // ===== Despacho polimórfico =====

    @Test
    @DisplayName("Una Troop se mapea con el TroopMapper y conserva sus stats")
    void dispatchesTroopToTroopMapper() {
        CardResponseDTO dto = cardMapper.toDto(knight());

        assertThat(String.valueOf(dto.getType())).isEqualTo("TROOP");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Knight");
        assertThat(dto.getElixirCost()).isEqualTo(3);
    }

    @Test
    @DisplayName("Un Spell se mapea con el SpellMapper")
    void dispatchesSpellToSpellMapper() {
        CardResponseDTO dto = cardMapper.toDto(fireball());

        assertThat(String.valueOf(dto.getType())).isEqualTo("SPELL");
        assertThat(dto.getName()).isEqualTo("Fireball");
    }

    @Test
    @DisplayName("Un Building se mapea con el BuildingMapper")
    void dispatchesBuildingToBuildingMapper() {
        CardResponseDTO dto = cardMapper.toDto(cannon());

        assertThat(String.valueOf(dto.getType())).isEqualTo("BUILDING");
        assertThat(dto.getName()).isEqualTo("Cannon");
    }

    /**
     * Este test es el que habría gritado "troopMapper is null" en segundos, en
     * vez de que apareciera como un 500 en Postman después de desplegar.
     */
    @Test
    @DisplayName("Los tres subtipos mezclados se mapean cada uno con su mapper")
    void mapsMixedListOfSubtypes() {
        List<Card> mixed = List.of(knight(), fireball(), cannon());

        List<CardResponseDTO> dtos = cardMapper.toDtoList(mixed);

        assertThat(dtos).hasSize(3);
        assertThat(dtos).extracting(d -> String.valueOf(d.getType()))
                .containsExactly("TROOP", "SPELL", "BUILDING");
        assertThat(dtos).extracting(CardResponseDTO::getName)
                .containsExactly("Knight", "Fireball", "Cannon");
    }

    // ===== El deploymentType (lo que hace que los hechizos vuelen libres) =====

    /**
     * Si este campo no llega al DTO, Game Engine nunca lo recibe y los
     * hechizos vuelven a estar restringidos a tu mitad del tablero — el bug
     * que ya arreglamos, pero por el camino de la BD.
     */
    @Test
    @DisplayName("El deploymentType viaja al DTO: sin el, los hechizos se rompen")
    void deploymentTypeReachesTheDto() {
        assertThat(String.valueOf(cardMapper.toDto(fireball().getClass()
                .cast(fireball())).getDeploymentType())).isEqualTo("ANYWHERE");
        assertThat(String.valueOf(cardMapper.toDto(knight()).getDeploymentType()))
                .isEqualTo("OWN_SIDE");
    }

    // ===== Bordes =====

    @Test
    @DisplayName("Mapear null devuelve null")
    void mapsNullToNull() {
        assertThat(cardMapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Una lista null devuelve lista vacia, no NPE")
    void mapsNullListToEmpty() {
        assertThat(cardMapper.toDtoList(null)).isEmpty();
    }

    /**
     * Si mañana alguien agrega un subtipo de Card y olvida el mapper, esto
     * falla ruidosamente en vez de devolver null en silencio.
     */
    @Test
    @DisplayName("Un subtipo desconocido lanza excepcion explicita")
    void unknownSubtypeThrows() {
        Card anonimo = new Card() {}; // subtipo sin mapper

        assertThatThrownBy(() -> cardMapper.toDto(anonimo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unknown Card subtype");
    }
}