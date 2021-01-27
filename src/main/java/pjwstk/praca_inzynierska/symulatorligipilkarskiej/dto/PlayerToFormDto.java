package pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerToFormDto {
    private Long id;
    private String name;
    private String surname;
}
