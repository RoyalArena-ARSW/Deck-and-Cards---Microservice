package edu.eci.arsw.RoyalArena.model;

import edu.eci.arsw.RoyalArena.model.enums.Target;

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
@DiscriminatorValue("BUILDING")
public class Building extends Card {

    /**
     * Puntos de vida de la estructura.
     */
    @Column(name = "health")
    private Integer health;

    /**
     * Daño por ataque. 0 si la estructura no ataca (Choza de Cabras solo genera tropas).
     */
    @Column(name = "damage")
    private Integer damage;

    /**
     * Duración en segundos antes de autodestruirse.
     * Todas las estructuras en CR tienen tiempo de vida limitado.
     */
    @Column(name = "lifetime_seconds")
    private Integer lifetimeSeconds;

    /**
     * Daño automático por segundo que la estructura se auto-inflige.
     */
    @Column(name = "self_damage_per_second")
    private Double selfDamagePerSecond;

    /**
     * Alcance de ataque en tiles. 0 si no ataca.
     */
    @Column(name = "attack_range")
    private Double attackRange;

    /**
     * Segundos entre ataques.
     */
    @Column(name = "attack_speed")
    private Double attackSpeed;

    /**
     * A qué unidades puede atacar.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private Target target;

    /**
     * True si genera tropas periódicamente (Horno, Choza de Cabras, Cementerio).
     */
    @Column(name = "is_spawner")
    private Boolean isSpawner;

    /**
     * Nombre de la tropa que genera, si isSpawner = true.
     * Ej: "Fire Spirit" para el Horno, "Goblin" para la Choza de Goblins.
     */
    @Column(name = "spawned_unit", length = 50)
    private String spawnedUnit;

    /**
     * Intervalo en segundos entre spawns de tropas.
     * Solo aplica si isSpawner = true. Ej: Goblin Hut = 4.7s.
     */
    @Column(name = "spawn_interval_seconds")
    private Double spawnIntervalSeconds;

    /**
     * Cuántas tropas genera por cada spawn.
     * Ej: Barbarian Hut = 1 barbarian, Furnace = 2 fire spirits.
     */
    @Column(name = "spawn_count")
    private Integer spawnCount;
}