package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto;

import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersDto {

    private List<Player> playerList = new ArrayList<>();

    public PlayersDto(List<Player> playerList) {
        this.playerList = playerList;
        System.out.println(this.playerList);


    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

}
