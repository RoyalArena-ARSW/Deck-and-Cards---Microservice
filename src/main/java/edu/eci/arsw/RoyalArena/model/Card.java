package edu.eci.arsw.RoyalArena.model;

import edu.eci.arsw.RoyalArena.model.enums.CardType;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cards")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    /**
     * Costo en elixir para desplegar la carta (1 a 10).
     */
    @Column(name = "elixir_cost", nullable = false)
    private int elixirCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;

    /**
     * Nivel actual de la carta (afecta stats). En Clash Royale va de 1 a 14.
     */
    @Column(nullable = false)
    private int level;

    /**
     * Arena en la que se desbloquea la carta (1 a 15+).
     */
    @Column(name = "unlock_arena", nullable = false)
    private int unlockArena;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    /**
     * Redundante con el discriminator column, pero útil para queries y respuestas al frontend
     * sin necesidad de hacer instanceof. El discriminator lo maneja Hibernate.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, insertable = false, updatable = false)
    private CardType type;
}