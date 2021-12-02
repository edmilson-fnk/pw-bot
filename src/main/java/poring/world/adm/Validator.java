package poring.world.adm;

import static poring.world.constants.Constants.ADM_ID;

public class Validator {

    public static boolean validAdmCall(double userId) {
        return userId == ADM_ID;
    }

}
