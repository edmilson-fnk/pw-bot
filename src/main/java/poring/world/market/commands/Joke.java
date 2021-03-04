package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.Constants.Jokes;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;

public class Joke extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        String query = Utils.getQuery(command);
        String joke = Jokes.getNamedJoke(query);
        event.getChannel().sendMessage(joke);
    }

    @Override
    public String getHelp() {
        return "get a joke";
    }

    @Override
    public List<String> getQueries() {
        return ImmutableList.of();
    }
}
