package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import org.springframework.stereotype.Controller;

@Controller
public class UserController {
/*

    @Autowired
    private UserRegister userService;
    @Autowired
    private UserRepository<Manager> userUserRepository;


    @GetMapping
    public String registerGet(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "security/register";
    }



  @PostMapping
    public String registerPost(@ModelAttribute User user, Model model) {
        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();


        if (user.getRole().getDescription().equals("ROLE_MANAGER")) {


            Manager manager = Manager.builder().username(user.getUsername()).password(UserRegister.encodePassword(password))
                    .repeatPassword(UserRegister.encodePassword(repeatPassword)).role(user.getRole()).build();

            userUserRepository.save(manager);


            return "redirect:/login";

        }


        if (!password.equals(repeatPassword)) {
            model.addAttribute("errorPassword", true);
            return "security/register";
        }
        model.addAttribute("registerSuccess", true);
        userService.registerNewUser(user);
        return "users/guest/index";
    }*/


}
