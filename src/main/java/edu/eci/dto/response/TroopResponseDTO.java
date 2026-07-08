package edu.eci.dto.response;


import com.royalarena.cards.model.enums.MovementSpeed;
import com.royalarena.cards.model.enums.Target;

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
public class TroopResponseDTO extends CardResponseDTO {

    private Integer damage;
    private Integer health;
    private Boolean isAerial;
    private Double attackSpeed;
    private MovementSpeed movementSpeed;
    private Double attackRange;
    private Target target;
    private Integer unitCount;
}