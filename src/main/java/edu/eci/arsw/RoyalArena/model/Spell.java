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
@DiscriminatorValue("SPELL")
public class Spell extends Card {

    /**
     * Daño que inflige al impactar (para hechizos de daño). 0 si es soporte (Curación).
     */
    @Column(name = "damage")
    private Integer damage;

    /**
     * Radio de efecto en tiles.
     */
    @Column(name = "effect_radius")
    private Double effectRadius;

    /**
     * Duración del efecto en segundos.
     * 0 para hechizos instantáneos (Bola de Fuego, Rayo).
     * >0 para hechizos con duración (Veneno, Hielo, Tornado).
     */
    @Column(name = "duration")
    private Double duration;

    /**
     * A qué unidades afecta el hechizo.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private Target target;

    /**
     * Tipo de efecto que aplica: daño instantáneo, continuo, ralentizar, curar, aturdir.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "effect_type")
    private EffectType effectType;
}