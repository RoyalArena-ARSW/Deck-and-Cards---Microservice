package edu.eci.arsw.RoyalArena.dto.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.eci.arsw.RoyalArena.model.enums.CardType;
import edu.eci.arsw.RoyalArena.model.enums.DeploymentType;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO base para respuestas de cartas. Usa @JsonTypeInfo para que el JSON
 * incluya un campo "type" que le dice al frontend a qué subclase pertenece
 * cada carta (útil al listar el contenido de un Deck con tipos mezclados).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = TroopResponseDTO.class, name = "TROOP"),
    @JsonSubTypes.Type(value = SpellResponseDTO.class, name = "SPELL"),
    @JsonSubTypes.Type(value = BuildingResponseDTO.class, name = "BUILDING")
})
public class CardResponseDTO {

    private Long id;
    private String name;
    private String description;
    private int elixirCost;
    private Rarity rarity;
    private int level;
    private int unlockArena;
    private String imageUrl;
    private CardType type;
    private DeploymentType deploymentType;
}