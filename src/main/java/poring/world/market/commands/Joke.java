package poring.world.market.commands;

import com.google.common.collect.ImmutableList;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import poring.world.constants.Jokes;
import poring.world.Utils;
import poring.world.market.Command;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Optional;

public class Joke extends Command {

    @Override
    public void run(String[] command, MessageCreateEvent event, Watcher watcher) {
        String query = Utils.getQuery(command);
        String joke = Jokes.getNamedJoke(query);

        Optional<Message> referencedMessage = event.getMessage().getReferencedMessage();
        if (referencedMessage.isPresent()) {
            referencedMessage.get().reply(joke);
        } else {
            event.getChannel().sendMessage(joke);
        }
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
