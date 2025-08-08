package com.ftc2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {
   private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
   private final Properties properties = new Properties();

   public static String requireNonBlank(String value, String name) {
      if (value == null || value.trim().isEmpty()) {
         throw new IllegalArgumentException("'" + name + "' must not be null, empty, or blank.");
      }
      return value.trim();
   }

   public static URL requireNonBlankURL(String rawUrl, String name) {
      if (rawUrl == null || rawUrl.trim().isEmpty()) {
         throw new IllegalArgumentException("'" + name + "' must not be null, empty, or blank.");
      }
      try {
         return new URL(rawUrl.trim());
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException("'" + name + "' must be a valid URL.", e);
      }
   }

   public ConfigLoader(String fileName) throws IOException {
      try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
         if (input == null) {
            throw new IOException("Config file '" + fileName + "' not found in classpath.");
         }
         properties.load(input);
         logger.info("Configuration loaded from {}", fileName);
      }
   }

   public String getDownloadDir() {
      return requireNonBlank(properties.getProperty("download_dir"), "download_dir");
   }

   public String getArchiveDir() {
      return requireNonBlank(properties.getProperty("archive_dir"), "archive_dir");
   }

   public URL getOneUrl() {
      return requireNonBlankURL(properties.getProperty("one_url"), "one_url");
   }

/**
 * Retrieves a list of endpoint URLs from the configuration property "endpoints".
 * Each URL in the list is validated and must be a well-formed URL.
 *
 * Note: The list is a comma separated list of URL strings. Duplicate entries are not checked.
 *
 * @return a list of valid URLs extracted from the "endpoints" property.
 * @throws IllegalArgumentException if the "endpoints" property is null, empty, or contains invalid URLs.
 */ /*
 */
   public List<URL> getEndpointUrls() {
     String endpointsRaw = properties.getProperty("endpoints");

     if (endpointsRaw == null || endpointsRaw.trim().isEmpty()) {
        throw new IllegalArgumentException("endpoints must not be null, empty, or blank.");
     }

     String[] parts = endpointsRaw.split(",");
      if (parts.length == 0) {
         throw new IllegalArgumentException("Configuration property 'endpoints' must contain at least one valid URL.");
      }
     List<URL> urls = new ArrayList<>();

     for (String part : parts) {
        String trimmed = part.trim();
        if (trimmed.isEmpty()) continue; // empty URLs are permitted in a list
        try {
           URL myUrl = new URL(trimmed);
           urls.add(myUrl);
        } catch (MalformedURLException e) {
           throw new IllegalArgumentException("Invalid URL in endpoints list: '" + trimmed + "' failed.", e);
        }
     }
     return urls;
   }
}

