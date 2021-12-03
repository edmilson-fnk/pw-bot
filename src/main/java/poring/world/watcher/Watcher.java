package poring.world.watcher;

import org.javacord.api.DiscordApi;
import poring.world.Fetcher;

public class Watcher {

  private final DiscordApi api;
  private final Fetcher fetcher;
  private WatcherThread watcherThread;

  public Watcher(DiscordApi api) {
    this.api = api;
    this.watcherThread = new WatcherThread(api);
    this.fetcher = new Fetcher();
  }

  public void start() {
    this.watcherThread.start();
  }

  public void restart() {
    this.watcherThread.interrupt();
    this.watcherThread = new WatcherThread(this.api);
    this.watcherThread.start();
  }

  public WatcherThread getWatcherThread() {
    return this.watcherThread;
  }

  public Fetcher getFetcher() {
    return fetcher;
  }
}
