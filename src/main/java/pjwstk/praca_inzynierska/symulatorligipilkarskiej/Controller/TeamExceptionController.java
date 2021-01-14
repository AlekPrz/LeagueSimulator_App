package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;
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


