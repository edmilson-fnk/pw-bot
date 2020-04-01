package poring.world;

import poring.world.market.ClearCommand;
import poring.world.market.Command;
import poring.world.market.HelpCommand;
import poring.world.market.ListCommand;
import poring.world.market.RemoveCommand;
import poring.world.market.SearchCommand;
import poring.world.market.URLCommand;
import poring.world.market.WatchCommand;

import java.util.HashMap;

public class Constants {

  // General
  public static final String MARKET_CALL = "gtb";
  public static final String BOT_URL = "bot_url";

  // Commands
  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String REMOVE = "remove";
  public static final String URL = "url";

  public static HashMap<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new HelpCommand());
    this.put(SEARCH, new SearchCommand());
    this.put(WATCH, new WatchCommand());
    this.put(CLEAR, new ClearCommand());
    this.put(LIST, new ListCommand());
    this.put(REMOVE, new RemoveCommand());
    this.put(URL, new URLCommand());
  }};

}
