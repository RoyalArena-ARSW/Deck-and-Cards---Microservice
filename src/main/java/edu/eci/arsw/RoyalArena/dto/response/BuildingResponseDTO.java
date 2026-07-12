package edu.eci.arsw.RoyalArena.dto.response;

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
public class BuildingResponseDTO extends CardResponseDTO {

    private Integer health;
    private Integer damage;
    private Integer lifetimeSeconds;
    private Double selfDamagePerSecond;
    private Double attackRange;
    private Double attackSpeed;
    private String target;
    private Boolean isSpawner;
    private String spawnedUnit;
    private Double spawnIntervalSeconds;
    private Integer spawnCount;
}