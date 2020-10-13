package pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods;

import java.util.Random;

public class PasswordGenerator {


    public static String stringGenerator() {

        StringBuilder sb = new StringBuilder();

        Random r = new Random();

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 5; i++) {
            sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        System.out.println(sb.toString());
        return sb.toString();

    }

}
