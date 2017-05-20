/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsApiImpl;

/**
 * The {@link JenkinsConnection} represents a connection to an {@link ExternalApi}.
 */
public class JenkinsConnection {

   private final String name;
   private final String location;
   private final String username;
   private final String password;
   private final ExternalApi api;
   
   /**
    * Constructs a new {@link JenkinsConnection}.
    * @param name the name of the {@link JenkinsConnection}, for reference, unique.
    * @param location the location of Jenkins.
    * @param username the username.
    * @param password the password.
    */
   public JenkinsConnection( String name, String location, String username, String password ) {
      this( name, location, username, password, new JenkinsApiImpl() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsConnection}.
    * @param name the name of the {@link JenkinsConnection}, for reference, unique.
    * @param location the location of Jenkins.
    * @param username the username.
    * @param password the password.
    * @param api the {@link ExternalApi}.
    */
   JenkinsConnection( String name, String location, String username, String password, ExternalApi api ) {
      this.name = name;
      this.location = location;
      this.username = username;
      this.password = password;
      this.api = api;
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
    * Access to the {@link ExternalApi} of the {@link JenkinsConnection}.
    * @return the {@link ExternalApi}.
    */
   public ExternalApi api() {
      return api;
   }//End Method
}//End Class
