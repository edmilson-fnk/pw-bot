package poring.world.listen;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

public class WatchObject {

  private String query;
  private MessageAuthor messageAuthor;
  private TextChannel channel;

  public WatchObject(String query, MessageAuthor messageAuthor, TextChannel channel) {
    this.query = query;
    this.messageAuthor = messageAuthor;
    this.channel = channel;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public MessageAuthor getMessageAuthor() {
    return messageAuthor;
  }

  public void setMessageAuthor(MessageAuthor messageAuthor) {
    this.messageAuthor = messageAuthor;
  }

  public TextChannel getChannel() {
    return channel;
  }

  public void setChannel(TextChannel channel) {
    this.channel = channel;
  }
}
