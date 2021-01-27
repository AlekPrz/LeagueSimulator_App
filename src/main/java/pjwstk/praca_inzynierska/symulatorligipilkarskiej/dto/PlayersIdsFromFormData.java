package pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayersIdsFromFormData {
    @Builder.Default
    private List<Long> ids = new ArrayList<>();
}
