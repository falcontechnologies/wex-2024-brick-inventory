package com.ftc2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Downloader {
   private static final Logger logger = LoggerFactory.getLogger(Downloader.class);

   public Downloader() {
   }

   public void downloadList(List<URL> urlList, Path downloadDir) {
      HttpClient client = HttpClient.newHttpClient();

      for (URL url : urlList) {
         String filename = Paths.get(url.getPath()).getFileName().toString();
         Path targetPath = downloadDir.resolve(filename);
         logger.info("Writing to {}", targetPath);
         try {
            HttpRequest request = HttpRequest.newBuilder()
               .uri(url.toURI())
               .GET()
               .build();

            logger.info("Downloading {} to {}", url, downloadDir.resolve(filename));
            HttpResponse<Path> response = client.send(
               request,
               HttpResponse.BodyHandlers.ofFile(targetPath));
            if (response.statusCode() == 200) {
               logger.info("Saved to: " + targetPath);
            } else {
               logger.info("Failed to download " + url + ": HTTP code " + response.statusCode());
            }
            // unzip it
            String outputName = filename.endsWith(".gz")
               ? filename.substring(0, filename.length() - 3)
               : filename + ".out"; // fallback if no .gz extension
            Path outputFile = targetPath.getParent() == null
               ? Path.of(outputName)
               : targetPath.getParent().resolve(outputName);
            InputStream in = new GZIPInputStream(Files.newInputStream(targetPath));
            Files.copy(in, outputFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            logger.info("Unzipped to: " + outputFile);
         } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
         }
      }
   }
}
