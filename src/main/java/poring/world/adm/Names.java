package poring.world.adm;

import com.amazonaws.util.StringUtils;
import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Names extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        Map<Long, List<WatchObject>> m = watcher.getWatcherThread().getMapReadonly();

        List<String> names = new LinkedList<>();
        for (Long id : m.keySet()) {
            List<WatchObject> list = m.get(id);
            if (!list.isEmpty()) {
                String name = list.get(0).getMessageAuthorName();
                names.add(name + " (_" + id + "_)");
            }
        }

        if (names.isEmpty()) {
            event.getChannel().sendMessage("No users found.");
        } else {
            event.getChannel().sendMessage(
                    "Users: " + StringUtils.join(", ", names.toArray(new String[]{}))
            );
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
