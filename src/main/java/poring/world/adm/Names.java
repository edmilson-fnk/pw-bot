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

public class Names extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        String query = Utils.getQuery(command);
        int lowerLimit;
        try {
            lowerLimit = Integer.parseInt(query);
        } catch (Exception e) {
            return;
        }

        Map<Long, List<WatchObject>> m = watcher.getWatcherThread().getMapReadonly();

        List<String> names = new LinkedList<>();
        for (Long id : m.keySet()) {
            List<WatchObject> list = m.get(id);
            if (list.size() > lowerLimit) {
                String name = list.get(0).getMessageAuthorName();
                names.add(name + " (_" + id + "_): " + list.size());
            }
        }

        if (names.isEmpty()) {
            event.getChannel().sendMessage("No users found.");
        } else {
            String usersList = "Users:\n" + StringUtils.join("\n", names.toArray(new String[]{}));
            event.getChannel().sendMessage(usersList);
        }
    }

    @Override
    public String getHelp() {
        return "shows who uses GTB";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("");
    }

}
