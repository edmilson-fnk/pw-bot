package poring.world.adm;

import poring.world.market.Command;
import poring.world.market.commands.Help;

import java.util.Map;

import static poring.world.constants.Constants.ADMIN_MAP;

public class AdmHelp extends Help {

    @Override
    public Map<String, Command> getMap() {
        return ADMIN_MAP;
    }

}
