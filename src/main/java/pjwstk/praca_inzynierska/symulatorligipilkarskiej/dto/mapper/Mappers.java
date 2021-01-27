package pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto.mapper;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.dto.PlayerToFormDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;

public interface Mappers {
    static PlayerToFormDto fromPlayerToPlayerToFormDto(Player player) {
        return PlayerToFormDto
                .builder()
                .id(player.getId())
                .name(player.getName())
                .surname(player.getSurname())
                .build();
    }
}
