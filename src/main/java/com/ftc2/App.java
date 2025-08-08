package com.ftc2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
   private static final Logger logger = LoggerFactory.getLogger(App.class);

   public static void main(String[] args) {
      logger.info("Application started.");

      try {
         ConfigLoader config = new ConfigLoader("config.properties");
         Downloader downloader = new Downloader();

         // a bunch of variables...
         String downloads = config.getDownloadDir();
         String archived = config.getArchiveDir();
         URL oneUrl = config.getOneUrl();
         List<URL> endpointUrls = config.getEndpointUrls();
         logger.info("dumping URL list: {}", endpointUrls.toString());

         logger.info("Precondition check passed. Proceeding with application logic.");

         Path downloadDir = Paths.get(downloads);
         Path archivedDir = Paths.get(archived);

         checkDir(downloadDir);
         checkDir(archivedDir);

         // download the file(s) and unzip
         downloader.downloadList(endpointUrls, downloadDir);
         // call the database upsert/replace functionality
         // TODO
         // then archive the files in a TAR file with the current date.
         // TODO

         // checks and error handling:
         // a. how do we know when the data structure changes?
         // b.
         // optimizations or improvements:
         // 1. add some database table(s) to store the data file date
         // and the date of upsert/replace function
         // 2. overwrite/replace '.gz' files
         // 3. only upsert/replace changed files
         // 4. only upsert
         // 5. add some database reporting about the changes to the
         // database
         // 6. More tests
         // 7. Improved application structure

         logger.info("Application ended.");

      } catch (IOException ie) {
         logger.error("IO error: {}", ie.getMessage());
      } catch (Exception e) {
            logger.error("Application error: {}", e.getMessage());
      }
   }

   public static void checkDir(Path dirPath) throws IOException {
      if (!Files.exists(dirPath)) {
         Files.createDirectories(dirPath);
         logger.info("Created directory: {}", dirPath);
      }
   }
}