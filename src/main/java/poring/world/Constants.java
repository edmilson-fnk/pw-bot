package poring.world;

import com.google.common.collect.ImmutableMap;
import poring.world.cards.Cheapest;
import poring.world.market.Clear;
import poring.world.general.Command;
import poring.world.market.Help;
import poring.world.market.ListC;
import poring.world.market.Remove;
import poring.world.market.Search;
import poring.world.market.URL;
import poring.world.market.Watch;
import poring.world.thanatos.Join;

import java.util.HashMap;
import java.util.Map;

public class Constants {

  // General
  public static final String MARKET_CALL = "gtb";
  public static final String THANATOS_CALL = "tt";
  public static final String CARDS_CALL = "cheapestcards";
  public static final String CARDS_CALL_SHORT = "cc";
  public static final String BOT_URL = "bot_url";

  // Commands
  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String REMOVE = "remove";
  public static final String URL = "url";

  // Join
  public static final String JOIN = "join";

  // Card Colors
  public static Map<String, String> CARD_COLOR = new HashMap<String, String>(){{
    this.put("blue", "3");
    this.put("green", "2");
    this.put("white", "1");
    this.put("b", "3");
    this.put("g", "2");
    this.put("w", "1");
  }};

  public static Map<String, String> CARD_COLOR_NAME = new HashMap<String, String>(){{
    this.put("3", "blue");
    this.put("2", "green");
    this.put("1", "white");
  }};

  // !gtb command
  public static Map<String, Command> MARKET_COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new Help());
    this.put(SEARCH, new Search());
    this.put(WATCH, new Watch());
    this.put(CLEAR, new Clear());
    this.put(LIST, new ListC());
    this.put(REMOVE, new Remove());
    this.put(URL, new URL());
  }};

  // !tt command
  public static Map<String, Command> TT_COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(JOIN, new Join());
  }};

  // !cheapestcards command
  public static Map<String, Command> CARDS_COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(CARDS_CALL, new Cheapest());
    this.put(CARDS_CALL_SHORT, new Cheapest());
  }};

}
