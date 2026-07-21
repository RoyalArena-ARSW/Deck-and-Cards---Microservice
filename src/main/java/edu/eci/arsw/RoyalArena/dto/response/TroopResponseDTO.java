package edu.eci.arsw.RoyalArena.dto.response;



import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
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
public class TroopResponseDTO extends CardResponseDTO {

    private Integer damage;
    private Integer health;
    private Boolean isAerial;
    private Double attackSpeed;
    private MovementSpeed movementSpeed;
    private Double attackRange;
    private Target target;
    private Integer unitCount;
    private Double splashRadius;
}