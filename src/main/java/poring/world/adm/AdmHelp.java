package poring.world.adm;

import poring.world.market.Command;
import poring.world.market.commands.Help;

import java.util.Map;

import static poring.world.constants.Constants.*;

public class AdmHelp extends Help {

    @Override
    public Map<String, Command> getMap() {
        return ADMIN_MAP;
    }

    @Override
    public String getCall() {
        return ADM_CALL;
    }

}
