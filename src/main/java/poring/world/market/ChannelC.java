package poring.world.market;

import static poring.world.Constants.Constants.CHANNEL;
import static poring.world.Constants.Constants.CHANNEL_OPTIONS;
import static poring.world.Constants.Constants.GLOBAL_CALL;
import static poring.world.Constants.Constants.HELP;
import static poring.world.Constants.Constants.MVP_CARDS;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.model.Channel;
import poring.model.WatchingChannel;
import poring.world.Database;
import poring.world.Utils;
import poring.world.watcher.Watcher;

import java.util.List;

public class ChannelC extends Command {

  @Override
  public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
    TextChannel channel = event.getChannel();
    String discordId = channel.getIdAsString();
    String query = Utils.getQuery(command);

    if (!CHANNEL_OPTIONS.contains(query)) {
      channel.sendMessage(String.format("Invalid option: _%s_. Check !%s %s %s", query, GLOBAL_CALL, HELP, CHANNEL));
      return;
    }

    Database db = new Database();
    Channel dbChannel = db.findChannelByDiscordId(discordId);
    if (dbChannel == null) {
      dbChannel = new Channel().withDiscordId(discordId);
    }

    if (dbChannel.getList() == null) {
      dbChannel.setList(new WatchingChannel());
    }
    dbChannel.getList().add(query);
    db.save(dbChannel);
    db.save(dbChannel.getList());
    db.close();

    channel.sendMessage(String.format("Watching **%s** on <#%s>", query, discordId));
  }

  @Override
  public String getHelp() {
    return String.format("watches market and notifies channels if anything is found. Valid options: %s",
        String.join(",", CHANNEL_OPTIONS));
  }

  @Override
  public List<String> getQueries() {
    return ImmutableList.of(MVP_CARDS);
  }

}
