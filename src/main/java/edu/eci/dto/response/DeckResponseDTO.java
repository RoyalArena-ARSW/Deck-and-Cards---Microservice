package edu.eci.dto.response;


import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeckResponseDTO {

    private Long id;
    private String name;
    private Long userId;
    private Boolean isActive;

    /**
     * Lista polimórfica: cada elemento es TroopResponseDTO, SpellResponseDTO
     * o BuildingResponseDTO. Jackson agrega el campo "type" a cada uno
     * gracias al @JsonTypeInfo de CardResponseDTO.
     */
    private List<CardResponseDTO> cards;

    private double averageElixirCost;
    private LocalDateTime createdAt;
}