package edu.eci.arsw.RoyalArena.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "decks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"})
)
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * True si es el mazo actualmente seleccionado por el jugador para jugar.
     * Un jugador puede tener múltiples mazos guardados pero solo uno activo.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = false;

    /**
     * Relación Many-to-Many con Card. Un mazo tiene exactamente 8 cartas,
     * y una misma carta puede estar en muchos mazos (catálogo compartido).
     * FetchType.EAGER porque cuando pides un Deck casi siempre quieres ver
     * sus cartas.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "deck_cards",
        joinColumns = @JoinColumn(name = "deck_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    @Builder.Default
    private List<Card> cards = new ArrayList<>();


    /**
     * Calcula el elixir promedio del mazo — KPI clave en Clash Royale para
     * evaluar qué tan barato de jugar es un deck (~3.0 es ciclo rápido,
     * ~4.5 es medio, >5 es pesado). Se marca @Transient para que Hibernate
     * no intente persistirlo; se recalcula al vuelo.
     */
    @Transient
    public double getAverageElixirCost() {
        if (cards == null || cards.isEmpty()) return 0.0;
        return cards.stream()
                .mapToInt(Card::getElixirCost)
                .average()
                .orElse(0.0);
    }

    /**
     * Valida que el mazo tenga exactamente 8 cartas (regla de Clash Royale).
     */
    public boolean hasValidCardCount() {
        return cards != null && cards.size() == 8;
    }
}