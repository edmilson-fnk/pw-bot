package poring.world.constants;

import com.vdurmont.emoji.EmojiParser;
import poring.world.adm.*;
import poring.world.market.Command;
import poring.world.market.cheapest.Cards;
import poring.world.market.cheapest.Premium;
import poring.world.market.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Constants {

  public static final Boolean MAINTENANCE = false;

  public static final int MAXIMUM_NAMES = 10;

  // Emojis
  public static final String MAGNIFIER = EmojiParser.parseToUnicode(":mag:");
  public static final String CHECK = EmojiParser.parseToUnicode(":white_check_mark:");
  public static final String CARD = EmojiParser.parseToUnicode(":black_joker:");
  public static final String GLASSES = EmojiParser.parseToUnicode(":eyeglasses:");
  public static final String HANDSHAKE = EmojiParser.parseToUnicode(":handshake:");
  public static final String X = EmojiParser.parseToUnicode(":x:");

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
  public static final String DRAKE = "drake";

  public static final String QUERY_SPLIT_TOKEN = "&&";

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
    this.put(REFINE_GT, "Refine >=");
    this.put(REFINE_LT, "Refine <=");
    this.put(NUM_SLOTS, "Slots");
    this.put(CATEGORY, "Category");
  }};
  public static final Map<String, String> CATEGORY_MAP = new HashMap<String, String>(){{
    this.put("weapon", "1025");
    this.put("off-hand", "1026");
    this.put("armor", "1027");
    this.put("garment", "1028");
    this.put("footgear", "1029");
    this.put("accessory", "1030");
  }};
  public static final Set<String> QUERY_FILTERS = FILTERS_NAME.keySet();

  // General Stuff
  public static final String ENV =
      System.getenv("ENVIRONMENT") != null ? System.getenv("ENVIRONMENT") : "staging";
  public static final boolean IS_PRODUCTION = ENV.equalsIgnoreCase("production");

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
  public static Map<String, Float> COLOR_DUST = new HashMap<String, Float>(){{
    this.put("3", 50F);
    this.put("2", 20F);
    this.put("1", 10F);
  }};

  public static Map<String, String> CARD_COLOR_NAME = new HashMap<String, String>(){{
    this.put("3", "blue");
    this.put("2", "green");
    this.put("1", "white");
  }};

  // !gtb-adm command
  public static final String ADM_CALL = "gtbadm";
  public static final String METRICS = "metrics";
  public static final String NAMES = "names";
  public static final String NAMEDLIST = "namedlist";
  public static final String CLEARNAMEDLIST = "clear";
  public static final String RESET_GTB = "resetgtb";
  public static Map<String, Command> ADMIN_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new AdmHelp());
    this.put(URL, new URL());
    this.put(METRICS, new Metrics());
    this.put(NAMES, new Names());
    this.put(NAMEDLIST, new NamedList());
    this.put(CLEARNAMEDLIST, new ClearNamedList());
    this.put(RESET_GTB, new Reset());
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
    this.put(DRAKE, new Drake());

    // Cheapest
    this.put(CARDS_CALL, new Cards());
    this.put(PREMIUM, new Premium());
  }};

}
