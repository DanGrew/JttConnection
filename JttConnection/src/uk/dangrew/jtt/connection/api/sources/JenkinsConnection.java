/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.sources;

import org.apache.http.client.HttpClient;

/**
 * The {@link JenkinsConnection} represents a connection to an {@link ExternalApi}.
 */
public class JenkinsConnection {

   private final String name;
   private final String location;
   private final String username;
   private final String password;
   private final HttpClient client;
   
   /**
    * Constructs a new {@link JenkinsConnection}.
    * @param name the name of the {@link JenkinsConnection}, for reference, unique.
    * @param location the location of Jenkins.
    * @param username the username.
    * @param password the password.
    * @param client the {@link HttpClient} configured for this connection.
    */
   JenkinsConnection( String name, String location, String username, String password, HttpClient client ) {
      this.name = name;
      this.location = location;
      this.username = username;
      this.password = password;
      this.client = client;
   }//End Constructor

   /**
    * Access to the name of the {@link JenkinsConnection}.
    * @return the name.
    */
   public String name() {
      return name;
   }//End Method

   /**
    * Access to the location of the {@link JenkinsConnection}.
    * @return the location.
    */
   public String location() {
      return location;
   }//End Method

   /**
    * Access to the username of the {@link JenkinsConnection}.
    * @return the username.
    */
   public String username() {
      return username;
   }//End Method
   
   /**
    * Access to the password of the {@link JenkinsConnection}.
    * @return the password.
    */
   public String password() {
      return password;
   }//End Method

   /**
    * Access to the {@link HttpClient} of the {@link JenkinsConnection}.
    * @return the {@link HttpClient}.
    */
   public HttpClient client() {
      return client;
   }//End Method
}//End Class
