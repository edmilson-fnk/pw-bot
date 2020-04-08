package poring.world;

import poring.world.cheapest.Cards;
import poring.world.cheapest.Premium;
import poring.world.general.Command;
import poring.world.market.Clear;
import poring.world.market.Help;
import poring.world.market.ListC;
import poring.world.market.Remove;
import poring.world.market.Search;
import poring.world.market.Watch;
import poring.world.party.CreateParty;
import poring.world.party.EndParty;
import poring.world.party.JoinParty;
import poring.world.thanatos.Thanatos;

import java.util.HashMap;
import java.util.Map;

public class Constants {

  // General
  public static final String GLOBAL_CALL = "gtb";

  // Parameters
  public static final String BOT_URL = "bot_url";
  public static final String PARTIES = "parties";
  public static final String API = "api";

  // Subcommands
  public static final String CARDS_CALL = "cards";
  public static final String PREMIUM = "premium";
  public static final String HELP = "help";
  public static final String SEARCH = "search";
  public static final String WATCH = "watch";
  public static final String CLEAR = "clear";
  public static final String LIST = "list";
  public static final String REMOVE = "remove";
  public static final String URL = "url";

  // JoinParty
  public static final String CREATE_PARTY = "party";
  public static final String JOIN_PARTY = "join";
  public static final String END_PARTY = "end";

  // Thanatos Tower Dungeon
  public static final String THANATOS = "end";

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
  public static Map<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
    this.put(HELP, new Help());
    this.put(SEARCH, new Search());
    this.put(WATCH, new Watch());
    this.put(CLEAR, new Clear());
    this.put(LIST, new ListC());
    this.put(REMOVE, new Remove());
//    this.put(URL, new URL());

//    // Cheapest
    this.put(CARDS_CALL, new Cards());
    this.put(PREMIUM, new Premium());

    // Thanatos Tower Team
    this.put(THANATOS, new Thanatos());

    // Party
//    this.put(CREATE_PARTY, new CreateParty());
//    this.put(JOIN_PARTY, new JoinParty());
//    this.put(END_PARTY, new EndParty());
  }};

}
