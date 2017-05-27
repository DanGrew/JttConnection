/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.sources;

import static org.mockito.Mockito.mock;

import org.apache.http.client.HttpClient;

/**
 * Test version of {@link JenkinsConnection} that can be instantiated.
 */
public class TestJenkinsConnection extends JenkinsConnection {

   /**
    * Constructs a new {@link JenkinsConnection} with default values.
    */
   public TestJenkinsConnection() {
      super( "location", "username", "password", mock( HttpClient.class) );
   }//End Constructor
   
   /**
    * Constructs a new {@link TestJenkinsConnection}.
    * @param location the location value.
    * @param username the username value.
    * @param password the password value.
    * @param client the client value.
    */
   public TestJenkinsConnection( String location, String username, String password, HttpClient client ) {
      super( location, username, password, client );
   }//End Constructor

}//End Class
