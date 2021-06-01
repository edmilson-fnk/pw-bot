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
        String query = Utils.getQuery(command);
        Map<Long, List<WatchObject>> m = watcher.getWatcherThread().getMap();

        List<String> namedList = new LinkedList<>();
        for (List<WatchObject> list : m.values()) {
            if (!list.isEmpty()) {
                String name = list.get(0).getMessageAuthorName();
                if (query.equalsIgnoreCase(name)) {
                    namedList.addAll(list.stream().map(WatchObject::getQuery).collect(Collectors.toList()));
                }
            }
        }

        if (namedList.isEmpty()) {
            event.getChannel().sendMessage("No list found for _" + query + "_");
        } else {
            event.getChannel().sendMessage(
                    "List for " +
                            "_" + query + "_ "+
                            "(" + namedList.size() + "):\n - " +
                            StringUtils.join("\n - ", namedList.toArray(new String[]{}))
            );
        }
    }

    @Override
    public String getHelp() {
        return "shows metrics for the bot usage";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("");
    }

}
