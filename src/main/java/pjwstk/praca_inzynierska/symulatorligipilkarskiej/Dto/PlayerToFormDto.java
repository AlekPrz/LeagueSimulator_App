package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerToFormDto {
    private Long id;
    private String name;
    private String surname;
}
