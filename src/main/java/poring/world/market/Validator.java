package poring.world.market;

import static poring.world.Constants.COMMAND_MAP;

public class Validator {

  public static boolean isValidCommand(String[] command) {
    return COMMAND_MAP.containsKey(command[1]);
  }

}
