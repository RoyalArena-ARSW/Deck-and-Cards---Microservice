package edu.eci.arsw.RoyalArena.dto.request;



import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.MovementSpeed;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.model.enums.Target;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TroopRequestDTO {

    // ----- Campos heredados de Card -----
    @NotBlank(message = "Name is required")
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String description;

    @Min(value = 1, message = "Elixir cost must be between 1 and 10")
    @Max(10)
    private int elixirCost;

    @NotNull
    private Rarity rarity;

    @Min(1)
    private int level;

    @Min(1)
    private int unlockArena;

    private String imageUrl;

    // ----- Campos específicos de Troop -----
    @NotNull @Min(0)
    private Integer damage;

    @NotNull @Min(1)
    private Integer health;

    @NotNull
    private Boolean isAerial;

    @NotNull @DecimalMin("0.1")
    private Double attackSpeed;

    @NotNull
    private MovementSpeed movementSpeed;

    @NotNull @DecimalMin("0.0")
    private Double attackRange;

    @NotNull
    private Target target;

    @NotNull @Min(1)
    private Integer unitCount;

    private DeploymentType deploymentType;
}