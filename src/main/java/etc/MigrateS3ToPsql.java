package etc;

import poring.model.Author;
import poring.model.Item;
import poring.model.WatchList;
import poring.world.watcher.WatchObject;
import poring.world.watcher.Watcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MigrateS3ToPsql {

  public static void main(String[] args) {
    Watcher watcher = new Watcher(null);

    Map<Long, List<WatchObject>> watchMap = watcher.getMap();
    Map<Long, Map<String, Map<String, String>>> filtersMap = watcher.getFilters();

    for (Long authorId : watchMap.keySet()) {
      Map<String, Map<String, String>> authorFilters = filtersMap.getOrDefault(authorId, new HashMap<>());
      List<WatchObject> list = watchMap.get(authorId);
      if (list.size() > 0) {
        Author author = new Author()
            .withDiscordName(list.iterator().next().getMessageAuthorName())
            .withDiscordId(authorId.toString());

        WatchList watchList = new WatchList()
            .withAuthor(author)
            .withItems(new LinkedList<>());
        for (WatchObject obj : list) {
          Map<String, String> filters = authorFilters.getOrDefault(obj.toString(), new HashMap<>());

          Item i = new Item()
              .withQuery(obj.getQuery())
              .withList(watchList)
              .withFilters(filters);

          watchList.getItems().add(i);
        }
      }
    }
  }

}
