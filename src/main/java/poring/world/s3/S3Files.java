package poring.world.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class S3Files {

  private static final String BUCKET_NAME = "ved-gtb";
  private static final String WATCHER_MAP_KEY = "market-map/";
  private static final String WATCHER_MAP_DAT = "watcherMap.dat";

  public static void uploadWatchList(File file) {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_2)
        .build();

    s3Client.putObject(new PutObjectRequest(BUCKET_NAME, WATCHER_MAP_KEY + WATCHER_MAP_DAT, file));
  }

  public static File downloadWatchlist() {
    File watcherMapFile = new File(WATCHER_MAP_DAT);
    try {
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
          .withRegion(Regions.US_EAST_2)
          .build();

      S3Object object = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, WATCHER_MAP_KEY + WATCHER_MAP_DAT));
      FileUtils.copyInputStreamToFile(object.getObjectContent(), watcherMapFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return watcherMapFile;
  }
}
