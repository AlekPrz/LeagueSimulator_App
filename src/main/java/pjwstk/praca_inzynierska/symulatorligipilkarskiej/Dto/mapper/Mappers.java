package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.mapper;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.PlayerToFormDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

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
