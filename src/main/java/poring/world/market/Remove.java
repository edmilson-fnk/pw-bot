package poring.world.market;

import static poring.world.Constants.MARKET_CALL;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.general.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Remove extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher, Map<String, Object> parameters) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    String query = Utils.getQuery(command);
    List<WatchObject> objList = watcher.getMap().get(messageAuthor.getId());
    if (query == null || query.isEmpty()) {
      event.getChannel().sendMessage("No index specified, _" + messageAuthor.getName() +
          "_. Try _!" + MARKET_CALL + " help list_ for more information");
      return;
    }

    if (objList != null) {
      int pos = Integer.parseInt(query);
      WatchObject removed = objList.remove(pos - 1);
      watcher.saveMap();

      String sb = "Removed _" +
          removed.getQuery() +
          "_ for _" +
          messageAuthor.getName() +
          "_\n";
      event.getChannel().sendMessage(sb);
    } else {
      event.getChannel().sendMessage("No watch list for _" + messageAuthor.getName() + "_");
    }
  }

  @Override
  public String getHelp() {
    return "removes selected item from you watch list. try _!" + MARKET_CALL + " list_ to check the index before removing";
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(
        "2"
    );
  }

}
