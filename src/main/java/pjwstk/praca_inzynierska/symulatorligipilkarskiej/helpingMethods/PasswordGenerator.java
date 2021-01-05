package pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class PasswordGenerator {


    public static String stringGenerator() {


        String result = RandomStringUtils.randomAlphabetic(6);

        return result;
    }


}

