package com.ftc2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

   /**
    * Tests for ConfigLoader#getEndpointUrls which retrieves and validates a list of endpoint URLs
    * from a configuration file. The method ensures the property is non-empty, well-formed, and valid.
    */

   @Test
   void testGetEndpointUrls_validMultipleUrls() throws IOException {
      ConfigLoader configLoader = new ConfigLoader("valid-endpoints.properties");
      List<URL> endpointUrls = configLoader.getEndpointUrls();

      assertEquals(3, endpointUrls.size());
      assertEquals("https://example.com/endpoint1", endpointUrls.get(0).toString());
      assertEquals("https://example.com/endpoint2", endpointUrls.get(1).toString());
      assertEquals("https://example.com/endpoint3", endpointUrls.get(2).toString());
   }

   @Test
   void testGetEndpointUrls_validSingleUrl() throws IOException {
      ConfigLoader configLoader = new ConfigLoader("single-endpoint.properties");
      List<URL> endpointUrls = configLoader.getEndpointUrls();

      assertEquals(1, endpointUrls.size());
      assertEquals("https://example.com/endpoint1", endpointUrls.get(0).toString());
   }

   @Test
   void testGetEndpointUrls_invalidUrl() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
         ConfigLoader configLoader = new ConfigLoader("invalid-endpoints.properties");
         configLoader.getEndpointUrls();
      });

      assertTrue(exception.getMessage().contains("Invalid URL in endpoints list"));
   }

   @Test
   void testGetEndpointUrls_emptyEndpointsProperty() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
         ConfigLoader configLoader = new ConfigLoader("empty-endpoints.properties");
         configLoader.getEndpointUrls();
      });

      assertTrue(exception.getMessage().contains("endpoints must not be null, empty, or blank."));
   }

   @Test
   void testGetEndpointUrls_missingEndpointsProperty() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
         ConfigLoader configLoader = new ConfigLoader("missing-endpoints.properties");
         configLoader.getEndpointUrls();
      });

      assertTrue(exception.getMessage().contains("endpoints must not be null, empty, or blank."));
   }

   @Test
   void testGetEndpointUrls_validWithExtraWhitespace() throws IOException {
      ConfigLoader configLoader = new ConfigLoader("whitespace-endpoints.properties");
      List<URL> endpointUrls = configLoader.getEndpointUrls();

      assertEquals(2, endpointUrls.size());
      assertEquals("https://example.com/endpoint1", endpointUrls.get(0).toString());
      assertEquals("https://example.com/endpoint2", endpointUrls.get(1).toString());
   }
}