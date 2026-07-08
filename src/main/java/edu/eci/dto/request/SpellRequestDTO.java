package edu.eci.dto.request;


import com.royalarena.cards.model.enums.EffectType;
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
public class SpellRequestDTO {

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

    // ----- Campos específicos de Spell -----
    @NotNull @Min(0)
    private Integer damage;

    @NotNull @DecimalMin("0.5")
    private Double effectRadius;

    @NotNull @DecimalMin("0.0")
    private Double duration;

    @NotNull
    private Target target;

    @NotNull
    private EffectType effectType;
}