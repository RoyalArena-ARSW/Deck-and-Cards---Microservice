package edu.eci.arsw.RoyalArena.dto.response;

import edu.eci.arsw.RoyalArena.model.enums.EffectType;
import edu.eci.arsw.RoyalArena.model.enums.Target;

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
public class SpellResponseDTO extends CardResponseDTO {

    private Integer damage;
    private Double effectRadius;
    private Double duration;
    private Target target;
    private EffectType effectType;
}