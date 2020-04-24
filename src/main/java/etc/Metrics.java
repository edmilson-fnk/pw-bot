package etc;

import static poring.world.Constants.ENV;

import poring.world.Utils;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.List;
import java.util.Map;

public class Metrics {

  public static void main(String[] args) {
    System.out.println("Getting metrics");
    System.out.println("Environment: " + ENV);

    Watcher w = new Watcher(null);
    w.loadMaps();
    Map<Long, List<WatchObject>> m = w.getMap();

    int numLists = 0;
    int numItens = 0;
    int maxSize = 0;
    int minSize = 99999;

    for (Long id : m.keySet()) {
      List<WatchObject> list = m.get(id);
      if (list.size() > 0) {
        numLists++;
        numItens += list.size();
        if (maxSize < list.size()) {
          maxSize = list.size();
        }
        if (minSize > list.size()) {
          minSize = list.size();
        }
      }
    }
    System.out.println(String.format("\nListas: %s\nTotal de itens: %s\nMaior lista: %s %s\nMenor lista: %s %s",
        numLists, numItens, maxSize, Utils.pluralItem(maxSize), minSize, Utils.pluralItem(minSize)));
  }

}
