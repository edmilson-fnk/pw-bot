package poring.world.watcher;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

public class WatchObject {

  private String query;
  private MessageAuthor messageAuthor;
  private long messageAuthorId;
  private TextChannel channel;
  private long channelId;

  public WatchObject(String query, MessageAuthor messageAuthor, TextChannel channel) {
    this.query = query;
    this.messageAuthor = messageAuthor;
    this.setMessageAuthorId(messageAuthor.getId());
    this.channel = channel;
    this.setChannelId(channel.getId());
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

  public long getMessageAuthorId() {
    return messageAuthorId;
  }

  public void setMessageAuthorId(long messageAuthorId) {
    this.messageAuthorId = messageAuthorId;
  }

  public long getChannelId() {
    return channelId;
  }

  public void setChannelId(long channelId) {
    this.channelId = channelId;
  }
}
