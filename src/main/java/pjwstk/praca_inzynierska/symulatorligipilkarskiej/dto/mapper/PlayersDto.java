package pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayersDto {

    List<Player> playerList;

}
