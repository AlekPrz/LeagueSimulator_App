package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Dto.mapper.PlayersDto;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.*;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.Counter;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MatchTeamRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MessageRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ContractService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.TeamService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ContractService contractService;
    private final MatchTeamRepository matchTeamRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MessageRepository messageRepository;

    @GetMapping("/")
    public String dash() {
        return "manager/dashboard";
    }

    @GetMapping("/mojaDruzyna")
    public String getMyTeam(Model model) {

        Manager manager = managerService.getCurrentManager();


        ManagerTeam managerTeam =
                manager.getManagerTeams()
                        .stream()
                        .filter(p -> p.getIsCurrently().equals(true))
                        .findFirst()
                        .orElse(null);


        if (managerTeam != null) {
            model.addAttribute("manager", managerTeam);
        } else {
            model.addAttribute("notInit", true);
        }


        return "manager/myTeam";

    }

    @GetMapping("/terminarzDruzyny")
    public String getSchedule(Model model) {


        List<MatchTeam> getMyTeamMatches = managerService.getCurrentMatches();


        List<Team> teams = managerService.getCurrentEnemy();


        Long idOfTeam;

        if (managerService.getCurrentPlayersOfTeam().isPresent()) {
            idOfTeam = managerService.getCurrentPlayersOfTeam().get().getId();
        } else {
            idOfTeam = null;
        }


        List<Player> players = teamService
                .getAllPlayersInThatTeam(idOfTeam);


        if (getMyTeamMatches != null && !getMyTeamMatches.isEmpty() && !players.isEmpty()) {
            model.addAttribute("matchTeam", getMyTeamMatches);
            model.addAttribute("players", players);
            model.addAttribute("sum", new Counter());
            model.addAttribute("managerUsername", managerService.getCurrentManager().getUsername());
        } else {
            model.addAttribute("matchTeamNotInit", true);
        }


        return "manager/schedule";

    }


    @GetMapping("/terminarzDruzyny/ustalSklad/{id}")
    public String chooseSquadOnMatch(@PathVariable Long id, Model model) {


        List<Player> players = teamService
                .getAllPlayersInThatTeam(managerService.getCurrentPlayersOfTeam().get().getId());

        List<Player> playersReadyOnMatch = new ArrayList<>();

        MatchTeam matchTeam = matchTeamRepository.findById(id).orElse(null);

        // Musze znaleźć aktualnie przypisanych graczy

        if (!Collections.disjoint(players, matchTeam.getHomeTeamPlayers())) {
            playersReadyOnMatch.addAll(matchTeam.getHomeTeamPlayers());
            model.addAttribute("homeTeamPlayersToDelete", true);
        }
        if (!Collections.disjoint(players, matchTeam.getVisitTeamPlayers())) {
            playersReadyOnMatch.addAll(matchTeam.getVisitTeamPlayers());
            model.addAttribute("visitTeamPlayersToDelete", true);
        }

        if (!playersReadyOnMatch.isEmpty()) {
            model.addAttribute("thereArePlayers", true);
            model.addAttribute("playersReadyOnMatch", playersReadyOnMatch);
        }


        model.addAttribute("form", new PlayersDto(players));
        model.addAttribute("matchTeam", matchTeam);


        return "manager/scheduleInsertSquad";
    }

    @PostMapping("/terminarzDruzyny/ustalSklad")
    public String chooseSquadOnMatchPost(@ModelAttribute PlayersDto playersIdsFromFormData, MatchTeam matchTeam) {


        List<Player> playersFromDb = playerService.getAllPlayers()
                .stream()
                .filter(second -> playersIdsFromFormData.getPlayerList().stream().allMatch
                        (p -> p.getId().equals(second.getId())))
                .collect(Collectors.toList());

        System.out.println(playersFromDb);


        MatchTeam matchTeam1 = matchTeamRepository.findById(matchTeam.getId()).orElse(null);


        for (Player player : playersFromDb) {
            if (matchTeam1.getHomeTeam().equals(managerService.getCurrentManagerTeam())) {
                player.getMatchTeamsHome().add(matchTeam1);
                matchTeam1.getHomeTeamPlayers().add(player);
            }
            if (matchTeam1.getVisitTeam().equals(managerService.getCurrentManagerTeam())) {
                player.getMatchTeamsVisit().add(matchTeam1);
                matchTeam1.getVisitTeamPlayers().add(player);

            }
            matchTeamRepository.save(matchTeam1);

        }


        return "redirect:/manager/terminarzDruzyny";
    }

    @PostMapping("/terminarzDruzyny/ustalSklad/usunSkladHome")
    public String deletePlayerMatch(Long homeTeamPlayersToDeleteId) {


        MatchTeam matchTeam1 = matchTeamRepository.findById(homeTeamPlayersToDeleteId).orElse(null);
        for (Player tmp : matchTeam1.getHomeTeamPlayers()) {
            matchTeam1.removeMatchTeamHome(tmp);
        }
        matchTeamRepository.save(matchTeam1);


        return "redirect:/manager/terminarzDruzyny";

    }
    @PostMapping("/terminarzDruzyny/ustalSklad/usunSkladVisit")
    public String deletePlayerVisit(Long visitTeamPlayersToDelete) {


        MatchTeam matchTeam1 = matchTeamRepository.findById(visitTeamPlayersToDelete).orElse(null);
        for (Player tmp : matchTeam1.getVisitTeamPlayers()) {
            matchTeam1.removeMatchTeamVisit(tmp);
        }
        matchTeamRepository.save(matchTeam1);


        return "redirect:/manager/terminarzDruzyny";

    }
/*

    @GetMapping("/wiadomosci/nowa")
    public String messagesDash(Model model) {

        model.addAttribute("manager", managerService.findManagers());
        model.addAttribute("message", new Message());
        model.addAttribute("howMuchNotRead", managerService.getNotRead());


        return "manager/messagesInsert";
    }

    @PostMapping("/sending")
    public String sendMessage(Message message) {


        messageRepository.save
                (Message.builder()
                        .text(message.getText())
                        .dateOfSend(LocalDate.now())
                        .isDeleteByReceiver(false)
                        .isDeletedBySender(false)
                        .isRead(false)
                        .userReceiver(managerService.findManagerById(message.getUserReceiver().getId()))
                        .userSender(managerService.getCurrentManager())
                        .subject(message.getSubject()).build());

        return "redirect:/manager/wiadomosci/odebrane";

    }

    @GetMapping("/wiadomosci/odebrane")
    public String messagesInbox(Model model) {


        List<Message> getAllNoDeletedMessages =
                managerService.getCurrentManager()
                        .getMessagesGot().stream()
                        .filter(p -> !p.getIsDeleteByReceiver())
                        .sorted(Comparator.comparing(Message::getDateOfSend).reversed())
                        .collect(Collectors.toList());


        model.addAttribute("messages", getAllNoDeletedMessages);
        model.addAttribute("howMuchNotRead", managerService.getNotRead());

        return "manager/messagesInBox";
    }


    @GetMapping("/wiadomosci/wyslane")
    public String messagesSent(Model model) {

        List<Message> getAllNoDeletedMessages =
                managerService.getCurrentManager()
                        .getMessagesSend().stream()
                        .filter(p -> !p.getIsDeletedBySender())
                        .sorted(Comparator.comparing(Message::getDateOfSend).reversed())
                        .collect(Collectors.toList());


        model.addAttribute("messages", getAllNoDeletedMessages);
        model.addAttribute("howMuchNotRead", managerService.getNotRead());


        return "manager/messagesInSent";
    }*/

 /*   @PostMapping("/messages/deleteSentMessage")
    public String deleteSentMessage(Long id) {

        System.out.println(id);

        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            message.get().setIsDeletedBySender(true);
            messageRepository.save(message.get());
        }


        return "redirect:/manager/wiadomosci/wyslane";
    }

    @PostMapping("/messages/deleteInBoxMessage")
    public String deleteInBoxMessage(Long id) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            message.get().setIsDeleteByReceiver(true);
            messageRepository.save(message.get());
        }

        return "redirect:/manager/wiadomosci/wyslane";
    }

    @GetMapping("/wiadomosci/kosz")
    public String messagesDeleted(Model model) {


        List<Message> getAllDeletedMessages = new ArrayList<>();


        Manager manager = managerService.getCurrentManager();


        for (Message tmp : manager.getMessagesSend()) {
            if (tmp.getIsDeletedBySender()) {
                getAllDeletedMessages.add(tmp);
            }
        }

        for (Message tmp : manager.getMessagesGot()) {
            if (tmp.getIsDeleteByReceiver()) {
                getAllDeletedMessages.add(tmp);
            }
        }


        model.addAttribute("messages", getAllDeletedMessages.stream().
                sorted(Comparator.comparing(Message::getDateOfSend).reversed()).collect(Collectors.toList())
        );
        model.addAttribute("howMuchNotRead", managerService.getNotRead());


        return "manager/messagesInTrash";
    }

    @GetMapping("/wiadomosci/odczytajWiadomosc/{id}")
    public String getDetatilsOfMessage(@PathVariable Long id, Model model) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            model.addAttribute("message", message.get());
            message.get().setIsRead(true);
            messageRepository.save(message.get());
        }


        return "manager/messageDetail";

    }

    @GetMapping("/wiadomosci/odczytWiadomosc/{id}")
    public String getDetailsOfSentMessage(@PathVariable Long id, Model model) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            model.addAttribute("message", message.get());
        }

        model.addAttribute("howMuchNotRead", managerService.getNotRead());

        return "manager/messageDetail";

    }*/




}
