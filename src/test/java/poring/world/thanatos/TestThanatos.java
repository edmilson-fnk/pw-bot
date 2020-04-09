package poring.world.thanatos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static poring.world.Constants.API;

import com.google.common.collect.ImmutableMap;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestThanatos {

  Map<String, Object> parameters;
  MessageCreateEvent event;

  @Before
  public void before() throws ExecutionException, InterruptedException {
    User user = mock(User.class);
    when(user.getDisplayName(any())).thenReturn("Usuario");

    CompletableFuture userOpt = mock(CompletableFuture.class);
    when(userOpt.get()).thenReturn(user);

    DiscordApi api = mock(DiscordApi.class);
    when(api.getServerById(anyLong())).thenReturn(null);
    when(api.getUserById(anyLong())).thenReturn(userOpt);

    parameters = ImmutableMap.of(API, api);

    TextChannel channel = mock(TextChannel.class);
    when(channel.sendMessage(anyString())).thenReturn(null);

    Server server = mock(Server.class);
    when(server.getId()).thenReturn(1L);
    when(server.getName()).thenReturn("Server Name");
    Optional<Server> serverOpt = Optional.of(server);

    MessageAuthor author = mock(MessageAuthor.class);
    when(author.getDisplayName()).thenReturn("Display author");
    when(author.getId()).thenReturn(1L);

    event = mock(MessageCreateEvent.class);
    when(event.getChannel()).thenReturn(channel);
    when(event.getServer()).thenReturn(serverOpt);
    when(event.getMessageAuthor()).thenReturn(author);
  }

  @Test
  public void test1() {

  }

}
