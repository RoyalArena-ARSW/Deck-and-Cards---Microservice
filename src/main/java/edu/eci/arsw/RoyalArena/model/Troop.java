package edu.eci.arsw.RoyalArena.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("TROOP")
public class Troop extends Card {

    /**
     * Daño que inflige por ataque.
     */
    @Column(name = "damage")
    private Integer damage;

    /**
     * Puntos de vida totales.
     */
    @Column(name = "health")
    private Integer health;

    /**
     * True si es una tropa aérea (Dragón Bebé, Esbirros), false si es terrestre.
     */
    @Column(name = "is_aerial")
    private Boolean isAerial;

    /**
     * Segundos entre ataques (ej. 1.5s para el Caballero).
     */
    @Column(name = "attack_speed")
    private Double attackSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_speed")
    private MovementSpeed movementSpeed;

    /**
     * Alcance de ataque en tiles. 0 para tropas melee.
     */
    @Column(name = "attack_range")
    private Double attackRange;

    /**
     * A qué unidades puede atacar (tierra, aire+tierra, solo edificios).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private Target target;

    /**
     * Cantidad de unidades que se despliegan al jugar la carta.
     * Ej: Esqueletos = 4, Goblins = 3, Gigante = 1.
     */
    @Column(name = "unit_count")
    private Integer unitCount;
}
