package edu.eci.arsw.RoyalArena.dto.response;


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

    private List<CardResponseDTO> cards;

    private double averageElixirCost;
}