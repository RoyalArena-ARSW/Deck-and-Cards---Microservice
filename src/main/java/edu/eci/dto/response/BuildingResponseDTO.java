package edu.eci.dto.response;

package com.royalarena.cards.dto.response;

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
public class BuildingResponseDTO extends CardResponseDTO {

    private Integer health;
    private Integer damage;
    private Integer lifetime;
    private Double attackRange;
    private Double attackSpeed;
    private Target target;
    private Boolean isSpawner;
    private String spawnedUnit;
}