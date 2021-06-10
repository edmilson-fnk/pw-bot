package poring.world.watcher;

import org.javacord.api.DiscordApi;

public class Watcher {

  private final DiscordApi api;
  private WatcherThread watcherThread;

  public Watcher(DiscordApi api) {
    this.api = api;
    this.watcherThread = new WatcherThread(api);
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

}
