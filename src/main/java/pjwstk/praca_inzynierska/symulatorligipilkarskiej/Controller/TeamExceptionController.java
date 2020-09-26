package pjwstk.praca_inzynierska.symulatorligipilkarskiej.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.TeamException;

@ControllerAdvice
public class TeamExceptionController {

 /*   @ExceptionHandler(value = TeamException.class)
    public String exception(TeamException exception, Model model) {

        model.addAttribute("es",exception.getMessage());
        return "error";
    }
*/
}
