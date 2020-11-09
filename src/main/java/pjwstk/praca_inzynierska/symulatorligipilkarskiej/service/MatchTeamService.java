package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.SeasonTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.SeasonTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.TeamRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchTeamService {

    private final MatchTeamRepository matchTeamRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;


    public void generateSchedule() {

        matchTeamRepository.deleteAll();


        Season season = Season.builder()
                .seasonStart(LocalDate.now())
                .seasonEnd(LocalDate.now().plusYears(1))
                .match(new LinkedHashSet<>())
                .seasonTeams(new LinkedHashSet<>()).build();


        seasonRepository.save(season);


        List<Team> allTeams = teamRepository.findAll();
        List<MatchTeam> matchTeamList1 = new ArrayList<>();


        for (Team team : allTeams) {

            SeasonTeam seasonTeam = SeasonTeam.builder().team(team).season(season).points(0).goals(0).matchesDone(0).isCurrently(true).currentlyPlace(0).build();


            seasonTeamRepository.save(seasonTeam);
        }


        int i = 3;
        int queue = 1;
        LocalDate localDate = LocalDate.of(2020, 11, 06);

        Random random = new Random();

        int firstRandom;
        int secondRandom;


        while (i != 0) {
            while (allTeams.size() != 0) {


                firstRandom = random.nextInt(allTeams.size());
                secondRandom = random.nextInt(allTeams.size());

                while (firstRandom == secondRandom) {
                    secondRandom = random.nextInt(allTeams.size());
                }
                Team firstTeam = allTeams.get(firstRandom);
                Team secondTeam = allTeams.get(secondRandom);

                MatchTeam matchTeam = MatchTeam.builder().homeTeam(firstTeam)
                        .visitTeam(secondTeam)
                        .season(season)
                        .queue(queue)
                        .dateOfGame(localDate)
                        .build();
                MatchTeam matchTeam1 = MatchTeam.builder().homeTeam(secondTeam).visitTeam(firstTeam)
                        .queue(queue)
                        .season(season)
                        .dateOfGame(localDate)
                        .build();

                while (matchTeamList1.contains(matchTeam) || matchTeamList1.contains(matchTeam1)) {


                    firstRandom = random.nextInt(allTeams.size());
                    secondRandom = random.nextInt(allTeams.size());
                    while (firstRandom == secondRandom) {

                        secondRandom = random.nextInt(allTeams.size());
                    }
                    matchTeam = MatchTeam.builder().homeTeam(allTeams.get(firstRandom)).visitTeam(allTeams.get(secondRandom))
                            .queue(queue).season(season).dateOfGame(localDate).build();
                    matchTeam1 = MatchTeam.builder().homeTeam(allTeams.get(secondRandom)).visitTeam(allTeams.get(firstRandom))
                            .queue(queue).season(season).dateOfGame(localDate).build();

                }

                if (!matchTeamList1.contains(matchTeam)) {


                    matchTeamList1.add(matchTeam);
                    matchTeamRepository.save(matchTeam);


                    if (firstRandom > secondRandom) {
                        allTeams.remove(firstRandom);
                        allTeams.remove(secondRandom);
                    } else {
                        allTeams.remove(secondRandom);
                        allTeams.remove(firstRandom);
                    }
                }

            }
            i--;
            queue++;
            localDate = localDate.plusWeeks(2);
            allTeams.addAll(teamRepository.findAll());
        }


    }


    public void generateTable(MatchTeam matchTeam) {

        String currentlyScore = matchTeam.getScore();

        String previousScore = matchTeamRepository.findById(matchTeam.getId()).orElse(null).getScore();


        if (currentlyScore != null && !currentlyScore.isEmpty()) {
            SeasonTeam firstTeam = seasonTeamRepository.findByTeam_Id(matchTeam.getHomeTeam().getId());
            SeasonTeam secondTeam = seasonTeamRepository.findByTeam_Id(matchTeam.getVisitTeam().getId());

            Integer firstTeamGoalsCurrently = Integer.parseInt(String.valueOf(currentlyScore.charAt(0)));
            Integer secondTeamGoalsCurrently = Integer.parseInt(String.valueOf(currentlyScore.charAt(2)));


            Integer firstTeamGoalsPrevious = 0;
            Integer secondTeamGoalsPrevious = 0;


            if (previousScore != null && !previousScore.isEmpty()) {
                firstTeamGoalsPrevious = Integer.parseInt(String.valueOf(previousScore.charAt(0)));
                secondTeamGoalsPrevious = Integer.parseInt(String.valueOf(previousScore.charAt(2)));

            }

            if (previousScore == null || previousScore.isEmpty()) {
                firstTeam.setMatchesDone(firstTeam.getMatchesDone() + 1);
                secondTeam.setMatchesDone(secondTeam.getMatchesDone() + 1);

            }

            firstTeam.setGoals(firstTeam.getGoals() - firstTeamGoalsPrevious + firstTeamGoalsCurrently);
            secondTeam.setGoals(secondTeam.getGoals() - secondTeamGoalsPrevious + secondTeamGoalsCurrently);


            if (whoWon(firstTeamGoalsCurrently, secondTeamGoalsCurrently).equals("firstTeamWon")) {


                System.out.println("Teraz wygrała 1");

                if (previousScore == null || previousScore.isEmpty()) {
                    firstTeam.setPoints(firstTeam.getPoints() + 3);


                } else {

                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {


                        firstTeam.setPoints(firstTeam.getPoints() + 3);
                        secondTeam.setPoints(secondTeam.getPoints() - 3);

                    }
                    //w poprzednim był remis
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {


                        firstTeam.setPoints(firstTeam.getPoints() - 1 + 3);
                        secondTeam.setPoints(secondTeam.getPoints() - 1);


                        // w poprzednim wygrała 1?
                    } else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        //nic sie nie dzieje

                        // Mecz odbył się 1x
                    }
                }
            }
            //Teraz wygrała 2
            else if (whoWon(firstTeamGoalsCurrently, secondTeamGoalsCurrently).equals("secondTeamWon")) {

                System.out.println("Teraz wygrałą 2");

                if (previousScore == null || previousScore.isEmpty()) {
                    secondTeam.setPoints(secondTeam.getPoints() + 3);
                } else {

                    //w poprzednim wygrała 1
                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 3);
                        secondTeam.setPoints(secondTeam.getPoints() + 3);
                    }
                    // był remis poprzednio
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 1);
                        secondTeam.setPoints(secondTeam.getPoints() - 1 + 3);
                    }
                    // w poprzednim wygrała 2
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {
                    }
                    // Meczy odbył się 1x
                }
                // Jeśli był teraz remis to
            } else {


                if (previousScore == null || previousScore.isEmpty()) {
                    firstTeam.setPoints(firstTeam.getPoints() + 1);
                    secondTeam.setPoints(secondTeam.getPoints() + 1);
                } else {

                    System.out.println("Teraz jest remis");


                    //w poprzednim wygrała 1
                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 3 + 1);
                        secondTeam.setPoints(secondTeam.getPoints() + 1);
                    }
                    //w poprzednim wygrała 2
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() + 1);
                        secondTeam.setPoints(secondTeam.getPoints() - 3 + 1);
                    }
                    // w poprzednim był remis
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {

                    }
                }
            }
            seasonTeamRepository.save(firstTeam);
            seasonTeamRepository.save(secondTeam);
            setPlace();
        }


    }


    public String whoWon(Integer firstTeamGoals, Integer secondTeamGoals) {
        if (firstTeamGoals > secondTeamGoals) {
            return "firstTeamWon";
        } else if (firstTeamGoals < secondTeamGoals) {
            return "secondTeamWon";
        } else {
            return "draw";
        }
    }

    public void setPlace() {

        Integer w = 1;

        for (SeasonTeam tmp : seasonTeamRepository.findAllByOrderByPointsDescGoalsDesc()) {


            tmp.setCurrentlyPlace(w);
            seasonTeamRepository.save(tmp);
            w++;
        }

    }


}
