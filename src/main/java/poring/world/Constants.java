package poring.world;

import poring.world.command.ClearCommand;
import poring.world.command.Command;
import poring.world.command.HelpCommand;
import poring.world.command.ListCommand;
import poring.world.command.RemoveCommand;
import poring.world.command.SearchCommand;
import poring.world.command.WatchCommand;

import java.util.HashMap;

public class Constants {

  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String REMOVE = "remove";

  public static HashMap<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new HelpCommand());
    this.put(SEARCH, new SearchCommand());
    this.put(WATCH, new WatchCommand());
    this.put(CLEAR, new ClearCommand());
    this.put(LIST, new ListCommand());
    this.put(REMOVE, new RemoveCommand());
  }};

}
