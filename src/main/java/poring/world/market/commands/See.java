package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;
import poring.world.watcher.WatcherThread;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static poring.world.constants.Constants.*;

public class See extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        WatcherThread watcherThread = watcher.getWatcherThread();
        MessageAuthor messageAuthor = event.getMessageAuthor();
        String query = Utils.getQuery(command);
        List<WatchObject> objList = watcherThread.getMap().get(messageAuthor.getId());
        Map<String, Map<String, String>> filters = watcherThread.getFilters().get(messageAuthor.getId());
        if (query.isEmpty()) {
            event.getChannel().sendMessage(
                    String.format("No index, Try _!%s %s %s_ for more information", GLOBAL_CALL, HELP, SEE)
            );
            return;
        }

        if (objList != null && !objList.isEmpty()) {
            List<String> numbers = query.equalsIgnoreCase("all") ?
                    IntStream.rangeClosed(1, objList.size()).mapToObj(Integer::toString).collect(Collectors.toList()) :
                    new LinkedList<>(new HashSet<>(Arrays.asList(query.split(" "))));
            List<String> toRemove = new LinkedList<>();
            for (String num : numbers) {
                if (num.isEmpty()) {
                    toRemove.add(num);
                }
                try {
                    Integer.parseInt(num);
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage(
                            String.format("Invalid option **%s**\nPlease check _!%s %s %s_", query, GLOBAL_CALL, HELP, SEE)
                    );
                    return;
                }
            }
            numbers.removeAll(toRemove);
            for (Integer pos : numbers.stream().map(Integer::parseInt).sorted(Collections.reverseOrder()).collect(Collectors.toList())) {
                if (pos > objList.size()) {
                    event.getChannel().sendMessage(String.format("Maximum value to see is **%s**", objList.size()));
                    return;
                }
                WatchObject watchObject = objList.get(pos - 1);
                Map<String, String> itemFilters = filters.getOrDefault(watchObject.toString(), new HashMap<>());
                for (String name : Utils.getNames(watchObject.getQuery())) {
                    StringBuilder sb = new StringBuilder();
                    JSONArray items = watcher.getFetcher().query(name, itemFilters);
                    for (Object item : items.subList(0, Math.min(Search.MAX_RESULTS, items.size()))) {
                        sb.append(String.format("%s\n", Utils.getItemMessage((JSONObject) item)));
                    }
                    event.getChannel().sendMessage(sb.toString());
                }
            }

            event.getMessage().addReaction(CHECK);
        } else {
            event.getChannel().sendMessage(String.format("No watch list for _%s_", messageAuthor.getDisplayName()));
        }
    }

    @Override
    public String getHelp() {
        return "searches for items in your watch list";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("2", "4 5", "all");
    }
}
