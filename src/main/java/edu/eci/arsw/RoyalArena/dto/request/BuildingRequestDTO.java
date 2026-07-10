package edu.eci.dto.request;


import com.royalarena.cards.model.enums.Rarity;
import com.royalarena.cards.model.enums.Target;

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
public class BuildingRequestDTO {

    @NotBlank @Size(max = 50)
    private String name;

    @NotBlank @Size(max = 500)
    private String description;

    @Min(1) @Max(10)
    private int elixirCost;

    @NotNull
    private Rarity rarity;

    @Min(1)
    private int level;

    @Min(1)
    private int unlockArena;

    private String imageUrl;

    // ----- Campos específicos de Building -----
    @NotNull @Min(1)
    private Integer health;

    @NotNull @Min(0)
    private Integer damage;

    @NotNull @Min(1)
    private Integer lifetime;

    @NotNull @DecimalMin("0.0")
    private Double attackRange;

    @NotNull @DecimalMin("0.0")
    private Double attackSpeed;

    @NotNull
    private Target target;

    @NotNull
    private Boolean isSpawner;

    @Size(max = 50)
    private String spawnedUnit;
}