package poring.world.adm;

import com.amazonaws.util.StringUtils;
import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NamedList extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        String userIdStr = Utils.getQuery(command);
        if (userIdStr.isEmpty()) {
            event.getChannel().sendMessage("No ID provided");
            return;
        }

        long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (Exception e) {
            event.getChannel().sendMessage("Invalid user ID: _" + userIdStr + "_");
            return;
        }
        Map<Long, List<WatchObject>> m = watcher.getWatcherThread().getMap();

        List<String> namedList = m.get(userId).stream().map(WatchObject::getQuery).collect(Collectors.toCollection(LinkedList::new));

        if (namedList.isEmpty()) {
            event.getChannel().sendMessage("No list found for _" + userIdStr + "_");
        } else {
            event.getChannel().sendMessage(
                    "List for " +
                            "_" + userIdStr + "_ "+
                            "(" + namedList.size() + "):\n   " +
                            StringUtils.join("\n   ", namedList.toArray(new String[]{}))
            );
        }
    }

    @Override
    public String getHelp() {
        return "shows someone's list";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("1231");
    }

}
