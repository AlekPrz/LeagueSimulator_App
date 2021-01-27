package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;

@ControllerAdvice
@RequiredArgsConstructor
public class TeamExceptionController {

    private final ManagerService managerService;

    @ModelAttribute("howMuchRead")
    public Long test() {

        return managerService.getNotRead();
    }
}


