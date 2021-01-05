package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Message;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.MessageRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor

public class MessageController {
    private final ManagerService managerService;
    private final UserRepository<User> userUserRepository;

    private final MessageRepository messageRepository;


    @GetMapping("/wiadomosci/odebrane")
    public String messagesInbox(Model model) {


        List<Message> getAllNoDeletedMessages =
                managerService.getCurrentUser()
                        .getMessagesGot().stream()
                        .filter(p -> !p.getIsDeleteByReceiver())
                        .sorted(Comparator.comparing(Message::getDateOfSend).reversed())
                        .collect(Collectors.toList());


        model.addAttribute("messages", getAllNoDeletedMessages);
        model.addAttribute("howMuchNotRead", managerService.getNotRead());
        model.addAttribute("howMuchRead", managerService.getAllMessage());

        System.out.println("co tu sie dzieje?");

        return "message/messagesInBox";
    }


    @GetMapping("/wiadomosci/nowa")
    public String messagesDash(Model model) {


        System.out.println(managerService.getCurrentUser().getRole().getDescription());

        if (managerService.getCurrentUser().getRole().getDescription().equals("ROLE_FAN")) {

            model.addAttribute("users", userUserRepository.findAll().stream()
                    .filter(p -> p.getRole().getDescription().equals("ROLE_ADMIN")).collect(Collectors.toList()));
            model.addAttribute("message", new Message());
            model.addAttribute("howMuchNotRead", managerService.getNotRead());
            model.addAttribute("howMuchRead", managerService.getAllMessage());

        } else {
            model.addAttribute("users", userUserRepository.findAll().stream().sorted(
                    Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList()));
            model.addAttribute("message", new Message());
            model.addAttribute("howMuchNotRead", managerService.getNotRead());
            model.addAttribute("howMuchRead", managerService.getAllMessage());

        }


        return "message/messagesInsert";
    }

    @PostMapping("/messages/sending")
    public String sendMessage(Message message) {


        messageRepository.save
                (Message.builder()
                        .text(message.getText())
                        .dateOfSend(LocalDate.now())
                        .isDeleteByReceiver(false)
                        .isDeletedBySender(false)
                        .isRead(false)
                        .userReceiver(userUserRepository.findById(message.getUserReceiver().getId()).orElse(null))
                        .userSender(managerService.getCurrentUser())
                        .subject(message.getSubject()).build());

        return "redirect:/wiadomosci/odebrane";

    }

    @PostMapping("/messages/deleteSentMessage")
    public String deleteSentMessage(Long id) {

        System.out.println(id);

        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            message.get().setIsDeletedBySender(true);
            messageRepository.save(message.get());
        }


        return "redirect:/wiadomosci/wyslane";
    }

    @PostMapping("/messages/deleteInBoxMessage")
    public String deleteInBoxMessage(Long id) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            message.get().setIsDeleteByReceiver(true);
            messageRepository.save(message.get());
        }

        return "redirect:/wiadomosci/wyslane";
    }

    @GetMapping("/wiadomosci/kosz")
    public String messagesDeleted(Model model) {


        List<Message> getAllDeletedMessages = new ArrayList<>();


        User user = managerService.getCurrentUser();


        for (Message tmp : user.getMessagesSend()) {
            if (tmp.getIsDeletedBySender()) {
                getAllDeletedMessages.add(tmp);
            }
        }

        for (Message tmp : user.getMessagesGot()) {
            if (tmp.getIsDeleteByReceiver()) {
                getAllDeletedMessages.add(tmp);
            }
        }


        model.addAttribute("messages", getAllDeletedMessages.stream().
                sorted(Comparator.comparing(Message::getDateOfSend).reversed()).collect(Collectors.toList())
        );
        model.addAttribute("howMuchNotRead", managerService.getNotRead());
        model.addAttribute("howMuchRead", managerService.getAllMessage());


        return "message/messagesInTrash";
    }

    @GetMapping("/wiadomosci/odczytajWiadomosc/{id}")
    public String getDetatilsOfMessage(@PathVariable Long id, Model model) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            model.addAttribute("message", message.get());
            model.addAttribute("howMuchNotRead", managerService.getNotRead());
            model.addAttribute("howMuchRead", managerService.getAllMessage());


            message.get().setIsRead(true);
            messageRepository.save(message.get());
        }


        return "message/messageDetail";

    }

    @GetMapping("/wiadomosci/odczytWiadomosc/{id}")
    public String getDetailsOfSentMessage(@PathVariable Long id, Model model) {


        Optional<Message> message = messageRepository.findById(id);


        if (message.isPresent()) {
            model.addAttribute("message", message.get());
        }

        model.addAttribute("howMuchNotRead", managerService.getNotRead());
        model.addAttribute("howMuchRead", managerService.getAllMessage());

        return "message/messageDetail";

    }


    @GetMapping("/wiadomosci/wyslane")
    public String messagesSent(Model model) {

        List<Message> getAllNoDeletedMessages =
                managerService.getCurrentUser()
                        .getMessagesSend().stream()
                        .filter(p -> !p.getIsDeletedBySender())
                        .sorted(Comparator.comparing(Message::getDateOfSend).reversed())
                        .collect(Collectors.toList());


        model.addAttribute("messages", getAllNoDeletedMessages);
        model.addAttribute("howMuchNotRead", managerService.getNotRead());
        model.addAttribute("howMuchRead", managerService.getAllMessage());


        return "message/messagesInSent";
    }
}
