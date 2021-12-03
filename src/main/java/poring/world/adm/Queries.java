package poring.world.adm;

import com.amazonaws.util.StringUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Queries extends Command {

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

        Map<String, Integer> queries = new HashMap<>();
        for (Long id : m.keySet()) {
            List<WatchObject> list = m.get(id);
            for (WatchObject obj : list) {
                String lowerCaseQuery = obj.getQuery().toLowerCase();
                queries.put(lowerCaseQuery, queries.getOrDefault(lowerCaseQuery, 0) + 1);
            }
        }

        LinkedList<String> sortedQueries = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : queries.entrySet()) {
            if (entry.getValue() > lowerLimit) {
                sortedQueries.add("[" + entry.getValue() + "] " + entry.getKey());
            }
        }
        sortedQueries.sort(Comparator.comparing(Queries::getNum));

        List<List<String>> partitions = Lists.partition(sortedQueries, 10);
        event.getChannel().sendMessage("Queries:");
        for (List<String> partition : partitions) {
            event.getChannel().sendMessage(StringUtils.join("\n", partition.toArray(new String[]{})));
        }
    }

    // Returns negative so that sortedList is reversed
    public static Integer getNum(String name) {
        String strPattern = "\\[([0-9]*).*";
        Pattern p = Pattern.compile(strPattern);
        Matcher matcher = p.matcher(name);
        if (matcher.matches()) {
            String numStr = matcher.group(1);
            return -Integer.parseInt(numStr);
        }
        return 0;
    }

    @Override
    public String getHelp() {
        return "Shows most watched queries";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of("5");
    }
}
