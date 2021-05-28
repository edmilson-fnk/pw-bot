package poring.world.Constants;

import com.google.common.collect.ImmutableSet;
import poring.world.adm.Metrics;
import poring.world.adm.Reset;
import poring.world.adm.URL;
import poring.world.market.Command;
import poring.world.market.WatchAdv;
import poring.world.market.cheapest.Cards;
import poring.world.market.cheapest.Premium;
import poring.world.market.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Constants {

  public static final Boolean MAINTENANCE = false;

  // General
  public static final String GLOBAL_CALL = "gtb";
  public static final String YES = "yes";
  public static final String NO = "no";

  // Subcommands
  public static final String CARDS_CALL = "cards";
  public static final String JOKE = "joke";
  public static final String PREMIUM = "premium";
  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String ORGANIZE = "organize";
  public static final String REMOVE = "remove";
  public static final String URL = "url";
  public static final String ABOUT = "about";
  public static final String ALIVE = "alive";

  // Channel commands
  public static final String CHANNEL = "channel";
  public static final String MVP_CARDS = "mvpcards";
  public static final Set<String> CHANNEL_OPTIONS = ImmutableSet.of(MVP_CARDS, CLEAR);

  // WatcherThread filters
  public static final String FILTER_TOKEN = "::";
  public static final String MAX_PRICE = "maxprice";
  public static final String BROKEN = "broken";
  public static final String ENCHANT = "enchant";
  public static final String EXCEPT = "except";
  public static final String REFINE_GT = "refine>";
  public static final String REFINE_LT = "refine<";
  public static final String NUM_SLOTS = "slots";
  public static final String CATEGORY = "category";
  public static final Map<String, String> FILTERS_NAME = new HashMap<String, String>(){{
    this.put(MAX_PRICE, "Max price");
    this.put(BROKEN, "Broken");
    this.put(ENCHANT, "Enchantment");
    this.put(EXCEPT, "Except");
    this.put(REFINE_GT, "Refine");
    this.put(REFINE_LT, "Refine");
    this.put(NUM_SLOTS, "Slots");
    this.put(CATEGORY, "Category");
  }};
  public static final Map<String, Integer> CATEGORY_MAP = new HashMap<String, Integer>(){{
    this.put("Weapon", 1025);
    this.put("Off-hand", 1026);
    this.put("Armor", 1027);
    this.put("Garment", 1028);
    this.put("Footgear", 1029);
  }};
  public static final Set<String> QUERY_FILTERS = FILTERS_NAME.keySet();

  // General Stuff
  public static final int TIME_DIFF
      = System.getenv("TIME_DIFF") != null ? Integer.parseInt(System.getenv("TIME_DIFF")) : 0;
  public static final String ENV =
      System.getenv("ENVIRONMENT") != null ? System.getenv("ENVIRONMENT") : "staging";
  public static final boolean IS_PRODUCTION = ENV.equalsIgnoreCase("production");
  public static final String GENERAL_TIME_FORMAT = "dd/MM/yyyy HH:mm";

  public static final Set<String> MVP_CARDS_LIST = MvpCardsList.getAll();

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
  public static final String RESET_GTB = "resetgtb";
  public static Map<String, Command> ADMIN_MAP = new HashMap<String, Command>(){{
    this.put(URL, new URL());
    this.put(METRICS, new Metrics());
    this.put(RESET_GTB, new Reset());

    // under tests
    this.put(WATCH, new WatchAdv());
  }};

  // !gtb command
  public static Map<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    // Market
    this.put(HELP, new Help());
    this.put(JOKE, new Joke());
    this.put(SEARCH, new Search());
    this.put(WATCH, new Watch());
    this.put(CLEAR, new Clear());
    this.put(LIST, new ListC());
    this.put(ORGANIZE, new Organize());
    this.put(REMOVE, new Remove());
    this.put(ABOUT, new About());
    this.put(ALIVE, new Alive());
//    this.put(CHANNEL, new ChannelC());

    // Cheapest
    this.put(CARDS_CALL, new Cards());
    this.put(PREMIUM, new Premium());

    // MVP cards
//    this.put(MVP_CARDS, new ChannelC());
  }};

}
