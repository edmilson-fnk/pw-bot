package poring.world.market.channel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Constants.Constants;
import poring.world.Fetcher;
import poring.world.Utils;

public enum ChannelOptions {

  MVP_CARDS(Constants.MVP_CARDS) {

    public String getData() {
      StringBuilder sb = new StringBuilder();
      for (String mvpCard : Constants.MVP_CARDS_LIST) {
        JSONArray cardOnMarket = Fetcher.query(mvpCard);
        if (cardOnMarket.size() > 0) {
          for (Object item : cardOnMarket) {
            sb.append(String.format("%s\n", Utils.getItemMessage((JSONObject) item)));
          }

        }
      }

      return sb.toString();
    }

    public String getTitle() {
      return "MVP Cards on exchange";
    }

  };

  private String name;

  ChannelOptions(String name) {
    this.name = name;
  }

  public static ChannelOptions getByName(String name) {
    for (ChannelOptions i : ChannelOptions.values()) {
      if (name.equalsIgnoreCase(i.getName())) {
        return i;
      }
    }
    return null;
  }

  public String getName() {
    return this.name;
  }

  public abstract String getData();

  public abstract String getTitle();

}
