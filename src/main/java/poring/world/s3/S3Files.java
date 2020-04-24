package poring.world.s3;

import static poring.world.Constants.IS_PRODUCTION;

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

  private static final String BUCKET_NAME = IS_PRODUCTION ? "ved-gtb" : "ved-gtb-staging";
  private static final String KEY = "market-map/";
  public static final String WATCHER_MAP_DAT = "watcherMap.dat";
  public static final String WATCHER_FILTERS_DAT = "watcherFilters.dat";
  public static final String THANATOS_TEAM_DAT = "thanatosMap.dat";
  public static final String THANATOS_TIME_DAT = "thanatosTime.dat";

  public static void uploadWatchList(File file) {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_2)
        .build();

    s3Client.putObject(new PutObjectRequest(BUCKET_NAME, KEY + WATCHER_MAP_DAT, file));
  }

  public static void uploadFiltersList(File file) {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_2)
        .build();

    s3Client.putObject(new PutObjectRequest(BUCKET_NAME, KEY + WATCHER_FILTERS_DAT, file));
  }

  public static void uploadThanatosTeam(File file) {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_2)
        .build();

    s3Client.putObject(new PutObjectRequest(BUCKET_NAME, KEY + THANATOS_TEAM_DAT, file));
  }

  public static void uploadThanatosTime(File file) {
    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_2)
        .build();

    s3Client.putObject(new PutObjectRequest(BUCKET_NAME, KEY + THANATOS_TIME_DAT, file));
  }

  public static File getFile(String fileName) {
    File file = new File(fileName);
    try {
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
          .withRegion(Regions.US_EAST_2)
          .build();

      if (s3Client.doesObjectExist(BUCKET_NAME, KEY + fileName)) {
        S3Object object = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, KEY + fileName));
        FileUtils.copyInputStreamToFile(object.getObjectContent(), file);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }

}
