package poring.world;

import com.google.common.collect.ImmutableMap;
import poring.world.adm.Metrics;
import poring.world.market.cheapest.Cards;
import poring.world.market.cheapest.CardsAdv;
import poring.world.market.cheapest.Premium;
import poring.world.market.Command;
import poring.world.market.Clear;
import poring.world.market.Help;
import poring.world.market.ListC;
import poring.world.market.Organize;
import poring.world.market.Remove;
import poring.world.market.Search;
import poring.world.adm.URL;
import poring.world.market.Watch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Constants {

  // General
  public static final String GLOBAL_CALL = "gtb";
  public static final String YES = "yes";
  public static final String NO = "no";

  // Subcommands
  public static final String CARDS_CALL = "cards";
  public static final String PREMIUM = "premium";
  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String ORGANIZE = "organize";
  public static final String REMOVE = "remove";
  public static final String URL = "url";

  // Thanatos Tower Dungeon
  public static final String THANATOS = "tt";
  public static final String LEAVE = "leave";
  public static final String CALL = "call";
  public static final String A = "A";
  public static final String B = "B";
  public static final String BACKUP = "backup";
  public static final String RESET = "reset";

  // Watcher filters
  public static final String FILTER_TOKEN = "::";
  public static final String MAX_PRICE = "maxprice";
  public static final String BROKEN = "broken";
  public static final String ENCHANT = "enchant";
  public static final Map<String, String> FILTERS_NAME = ImmutableMap.of(
      MAX_PRICE, "Max price",
      BROKEN, "Broken",
      ENCHANT, "Enchantment"
  );
  public static final Set<String> QUERY_FILTERS = FILTERS_NAME.keySet();

  // General Stuff
  public static final int TIME_DIFF
      = System.getenv("TIME_DIFF") != null ? Integer.parseInt(System.getenv("TIME_DIFF")) : 0;
  public static final String ENV =
      System.getenv("ENVIRONMENT") != null ? System.getenv("ENVIRONMENT") : "staging";
  public static final boolean IS_PRODUCTION = ENV.equalsIgnoreCase("production");
  public static final String GENERAL_TIME_FORMAT = "dd/MM/yyyy HH:mm";

  // Parameters
  public static final String PARTIES = "parties";

  // Card Colors
  public static Map<String, String> CARD_COLOR = new HashMap<String, String>(){{
    this.put("blue", "3");
    this.put("green", "2");
    this.put("white", "1");
    this.put("b", "3");
    this.put("g", "2");
    this.put("w", "1");
  }};

  // Card Colors
  public static Map<String, Integer> COLOR_DUST = new HashMap<String, Integer>(){{
    this.put("3", 50);
    this.put("2", 20);
    this.put("1", 10);
  }};

  public static Map<String, String> CARD_COLOR_NAME = new HashMap<String, String>(){{
    this.put("3", "blue");
    this.put("2", "green");
    this.put("1", "white");
  }};

  // !gtb-adm command
  public static final String ADM_CALL = "gtbadm";
  public static final String METRICS = "metrics";
  public static Map<String, Command> ADMIN_MAP = new HashMap<String, Command>(){{
    this.put(URL, new URL());
    this.put(METRICS, new Metrics());
    this.put(CARDS_CALL, new CardsAdv());
  }};

  // !gtb command
  public static Map<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    // Market
    this.put(HELP, new Help());
    this.put(SEARCH, new Search());
    this.put(WATCH, new Watch());
    this.put(CLEAR, new Clear());
    this.put(LIST, new ListC());
    this.put(ORGANIZE, new Organize());
    this.put(REMOVE, new Remove());

    // Cheapest
    this.put(CARDS_CALL, new Cards());
    this.put(PREMIUM, new Premium());

    // Thanatos Tower Team
//    this.put(THANATOS, new Thanatos());
  }};

}
