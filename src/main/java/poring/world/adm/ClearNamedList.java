package poring.world.adm;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;
import poring.world.watcher.WatcherThread;

import java.util.List;
import java.util.Map;

public class ClearNamedList extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        String userIdStr = Utils.getQuery(command);
        if (userIdStr.isEmpty()) {
            event.getChannel().sendMessage("No ID provided");
            return;
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (Exception e) {
            event.getChannel().sendMessage("Invalid user ID: _" + userIdStr + "_");
            return;
        }

        WatcherThread watcherThread = watcher.getWatcherThread();

        if (watcherThread.getMap().containsKey(userId)) {
            List<WatchObject> authorList = watcherThread.getMap().get(userId);
            int size = 0;
            if (authorList != null) {
                size = authorList.size();
                authorList.clear();
            }
            Map<String, Map<String, String>> authorFilters = watcherThread.getFilters().get(userId);
            if (authorFilters != null) {
                authorFilters.clear();
            }
            watcherThread.saveMaps();
            event.getChannel().sendMessage(
                    String.format("Removed _%s_ %s for _%s_", size, Utils.pluralItem(size), userId)
            );
        }
    }

    @Override
    public String getHelp() {
        return "removes every query on someone's watch list";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("1231");
    }

}
