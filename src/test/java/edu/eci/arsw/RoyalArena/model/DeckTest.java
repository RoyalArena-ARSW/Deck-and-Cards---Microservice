package edu.eci.arsw.RoyalArena.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.model.enums.Target;

/**
 * El elixir promedio es @Transient: se calcula, no se guarda. Es la métrica
 * que distingue un mazo de ciclo rápido de uno de beatdown.
 */
class DeckTest {

    private static final Offset<Double> EPS = Offset.offset(0.01);

    private Troop troopCosting(long id, int cost) {
        return Troop.builder()
                .id(id).name("Card" + id).elixirCost(cost)
                .rarity(Rarity.COMMON).level(11).unlockArena(0)
                .deploymentType(DeploymentType.OWN_SIDE)
                .damage(100).health(500).isAerial(false)
                .attackSpeed(1.0).movementSpeed(MovementSpeed.MEDIUM)
                .attackRange(1.0).target(Target.GROUND).unitCount(1)
                .build();
    }

    private Deck deckWithCosts(int... costs) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < costs.length; i++) {
            cards.add(troopCosting(i + 1, costs[i]));
        }
        return Deck.builder()
                .id(1L).name("Test").userId(1L).cards(cards)
                .build();
    }

    @Test
    @DisplayName("El elixir promedio es la media de los costes")
    void averageElixirIsTheMean() {
        // Ciclo rápido: 3+3+2+1+3+3+3+4 = 22 / 8 = 2.75
        Deck fast = deckWithCosts(3, 3, 2, 1, 3, 3, 3, 4);
        assertThat(fast.getAverageElixirCost()).isCloseTo(2.75, EPS);

        // Beatdown: 3+2+4+4+4+5+4+5 = 31 / 8 = 3.875
        Deck heavy = deckWithCosts(3, 2, 4, 4, 4, 5, 4, 5);
        assertThat(heavy.getAverageElixirCost()).isCloseTo(3.875, EPS);
    }

    @Test
    @DisplayName("Es una division real, no entera")
    void averageIsNotIntegerDivision() {
        // 1+1+1+1+1+1+1+2 = 9 / 8 = 1.125 (no 1)
        assertThat(deckWithCosts(1, 1, 1, 1, 1, 1, 1, 2).getAverageElixirCost())
                .isCloseTo(1.125, EPS);
    }

    /**
     * Un mazo sin cartas no debería existir, pero si el cálculo divide entre
     * cero el endpoint entero se cae. Esta es la misma clase de borde que el
     * winRate de Profile.
     */
    @Test
    @DisplayName("Un mazo vacio no revienta el calculo del promedio")
    void emptyDeckDoesNotBlowUp() {
        Deck empty = Deck.builder().id(1L).name("Vacio").userId(1L)
                .cards(new ArrayList<>()).build();

        double avg = empty.getAverageElixirCost();

        assertThat(Double.isNaN(avg)).as("NaN se serializa raro en JSON").isFalse();
        assertThat(Double.isInfinite(avg)).isFalse();
        assertThat(avg).isZero();
    }

    @Test
    @DisplayName("Un mazo con cards null tampoco revienta")
    void nullCardsDoesNotBlowUp() {
        Deck deck = Deck.builder().id(1L).name("X").userId(1L).cards(null).build();
        assertThat(deck.getAverageElixirCost()).isZero();
    }
}