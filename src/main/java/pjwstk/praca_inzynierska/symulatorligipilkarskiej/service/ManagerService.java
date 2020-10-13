package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.Manager;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.User.User;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository<Manager> userRepository;



    public List<Manager> findManagers(){

        List<Manager> managers = new ArrayList<>();

        for (User tmp : userRepository.findAll()) {
            if (tmp instanceof Manager) {
                managers.add((Manager) tmp);
            }
        }

        return managers;

    }

}
