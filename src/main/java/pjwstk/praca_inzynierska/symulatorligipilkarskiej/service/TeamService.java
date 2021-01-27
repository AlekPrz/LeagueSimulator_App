package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.validator.TeamValidator;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamValidator teamValidator;
    private final SeasonTeamRepository seasonTeamRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final UserRepository<Manager> managerUserRepository;
    private final ManagerTeamRepository managerTeamRepository;


    public Map<String, String> checkErrors(Team team, ManagerTeam managerTeam, BindingResult bindingResult) {


        Map<String, String> errorsFromBinding
                = bindingResult
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (v1, v2) -> v1 + ", " + v2
                ));

        Map<String, String> errorsFromMyValidate = new LinkedHashMap<>();
        errorsFromMyValidate.putAll(teamValidator.validate(team, managerTeam));
        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }

    public Map<String, String> checkErrorsModify(Team team, ManagerTeam managerTeam, BindingResult bindingResult) {


        Map<String, String> errorsFromBinding
                = bindingResult
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getField(),
                        e -> e.getDefaultMessage(),
                        (v1, v2) -> v1 + ", " + v2
                ));

        Map<String, String> errorsFromMyValidate = new LinkedHashMap<>();
        errorsFromMyValidate.putAll(teamValidator.validateModify(team, managerTeam));
        System.out.println("jest walidacja");
        errorsFromBinding.forEach(errorsFromMyValidate::putIfAbsent);

        return errorsFromMyValidate;

    }


    public Team createTeam(Team team, ManagerTeam managerTeam) {

        Manager manager = managerTeam.getManager();
        teamRepository.save(team);
        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();


        managerTeamRepository.save(managerTeam1);
        manager.getManagerTeams().add(managerTeam);
        team.getManagerTeams().add(managerTeam);
        return team;

    }

    public Team modifyTeam(Team team, ManagerTeam managerTeam) {

        Manager manager = managerUserRepository.findById(managerTeam.getManager().getId()).orElse(null);
        ManagerTeam managerTeam1 = ManagerTeam.builder()
                .isCurrently(true)
                .startOfContract(managerTeam.getStartOfContract())
                .team(team)
                .manager(manager)
                .build();

        System.out.println(manager);

        if (teamChangedHisManager(manager.getId(), team.getId())) {
            System.out.println("zmienił?");
            ManagerTeam contractCurrently = findCurrentlyContract(team.getId());
            System.out.println("1?");
            contractCurrently.setIsCurrently(false);
            System.out.println("2?");

            managerTeamRepository.save(contractCurrently);
            managerTeamRepository.save(managerTeam1);
            return team;
        }

        if (findCurrentlyContract(team.getId()) == null) {
            team.getManagerTeams().add(managerTeam1);
            manager.getManagerTeams().add(managerTeam1);
            managerTeamRepository.save(managerTeam1);
            teamRepository.save(team);

            return team;
        }
        teamRepository.save(team);
        return team;


    }

    public boolean teamChangedHisManager(Long managerId, Long teamId) {

        Manager manager = findManagerByid(managerId);
        Team team = teamRepository.findById(teamId).orElse(null);


        for (ManagerTeam tmp : team.getManagerTeams()) {
            if (tmp.getIsCurrently() && !tmp.getManager().getUsername().equals(manager.getUsername())) {
                System.out.println("ZMIENIL DRUZYNE");
                return true;
            }
        }
        return false;

    }


    public void deleteTeam(Long id) {


        Team teamToDelete = teamRepository.findById(id).orElse(null);


        for (SeasonTeam tmp : seasonTeamRepository.findAll()) {
            if (tmp.getTeam().getId().equals(teamToDelete.getId())) {
                seasonTeamRepository.delete(tmp);

            }
        }
        for (MatchTeam tmp : matchTeamRepository.findAll()) {
            if (tmp.getHomeTeam().getId().equals(teamToDelete.getId())) {
                matchTeamRepository.delete(tmp);
            }
            if (tmp.getVisitTeam().getId().equals(teamToDelete.getId())) {
                matchTeamRepository.delete(tmp);
            }
        }


        teamRepository.delete(teamToDelete);


    }


    public List<Team> getAllTeam() {
        return teamRepository
                .findAll();
    }

    public List<Team> findByKeyword(String keyword) {
        return teamRepository.findByKeyword(keyword);
    }


    public List<Player> getAllPlayersInThatTeam(Long id) {


        if (id == null) {

            return new ArrayList<>();
        }

        List<Player> playersInThatTeam = new ArrayList<>();

        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            throw new RuntimeException("team is null");
        }


        for (Contract tmp : team.getContracts()) {
            if (tmp.getIsCurrently()) {
                playersInThatTeam.add(tmp.getPlayer());
            }
        }

        return playersInThatTeam;

    }

    public ManagerTeam findCurrentlyManagerTeam(Long id) {

        Team team = teamRepository.findById(id).orElse(null);

        System.out.println("yes1?");

        ManagerTeam managerTeam = null;

        for (ManagerTeam tmp : team.getManagerTeams()) {
            if (tmp.getIsCurrently()) {
                managerTeam = tmp;
            }
        }


        if (managerTeam == null) {
            return null;
        }

        return managerTeam;
    }

    public Manager findManagerByid(Long id) {
        return managerUserRepository.findById(id).orElse(null);
    }


    public ManagerTeam findCurrentlyContract(Long id) {

        Team team = teamRepository.findById(id).orElse(null);
        ManagerTeam contract = null;

        for (ManagerTeam tmp : team.getManagerTeams()) {
            if (tmp.getIsCurrently()) {
                contract = tmp;
            }
        }


        return contract;
    }


}