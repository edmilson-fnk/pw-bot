package poring.world.watcher;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

import java.io.Serializable;

public class WatchObject implements Serializable {

  private String query;
  private String messageAuthorName;
  private long messageAuthorId;
  private long channelId;

  public WatchObject(String query, MessageAuthor messageAuthor, TextChannel channel) {
    this.setQuery(query);
    this.setMessageAuthorName(messageAuthor.getDisplayName());
    this.setMessageAuthorId(messageAuthor.getId());
    this.setChannelId(channel.getId());
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
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

  public String getMessageAuthorName() {
    return messageAuthorName;
  }

  public void setMessageAuthorName(String messageAuthorName) {
    this.messageAuthorName = messageAuthorName;
  }

  @Override
  public String toString() {
    return "Query=" + this.query + ";" +
        "AuthorName=" + this.messageAuthorName + ";" +
        "AuthorID=" + this.messageAuthorId + ";" +
        "ChannelID=" + this.channelId;
  }
}
