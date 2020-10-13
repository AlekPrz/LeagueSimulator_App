package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice // kontroler globalny pozwalający na przechwytywanie błedów z innych kontrolerów

public class ExceptionController {

    @ExceptionHandler(RuntimeException.class) // adnotacja która pozwala zarządzać błedami
    public String myExceptionHandler(RuntimeException e, Model model) {
        model.addAttribute("exceptionMessage", e.getMessage());
        return "/security/exceptionPage";
    }

}
