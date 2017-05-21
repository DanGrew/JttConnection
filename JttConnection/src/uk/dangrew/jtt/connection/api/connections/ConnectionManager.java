/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;

/**
 * The {@link ConnectionManager} is responsible for managing the {@link JenkinsConnection}s in the system
 * and access to them.
 */
public class ConnectionManager {

   private final ConnectionActivator activator;
   private final Set< JenkinsConnection > connections;
   
   /**
    * Constructs a new {@link ConnectionManager}.
    */
   public ConnectionManager() {
      this( new ConnectionActivator() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ConnectionManager}.
    * @param activator the {@link ConnectionActivator} performing activation.
    */
   ConnectionManager( ConnectionActivator activator ) {
      this.connections = new LinkedHashSet<>();
      this.activator = activator;
   }//End Constructor
   
   /**
    * Method to verify that a {@link JenkinsConnection} exists for the given.
    * @param connection the {@link JenkinsConnection} in question.
    */
   private void verifyExists( JenkinsConnection connection ) {
      if ( !connections.contains( connection ) ) {
         throw new IllegalArgumentException( "Connection does not exist: " + connection.location() );
      }
   }//End Method
   
   /**
    * Access to the {@link JenkinsConnection}s present, but not necessarily active.
    * @return a {@link List} of {@link JenkinsConnection}s.
    */
   public List< JenkinsConnection > connections() {
      return new ArrayList<>( connections );
   }//End Method
   
   /**
    * Method to make a connection with the given credentials.
    * @param location the location of the jenkins instance.
    * @param username the username to login with.
    * @param password the password to login with.
    * @return the {@link JenkinsConnection} for a successful login, null otherwise.
    */
   public JenkinsConnection makeConnection( String location, String username, String password ) {
      JenkinsConnection connection = activator.makeConnection( location, username, password );
      if ( connection != null ) {
         connections.add( connection );
      }
      return connection;
   }//End Method

   /**
    * Method to connect the {@link JenkinsConnection} with the given name.
    * @param connection the {@link JenkinsConnection} to connect.
    */
   public void connect( JenkinsConnection connection ) {
      verifyExists( connection );
      activator.connect( connection );
   }//End Method

   /**
    * Method to disconnect the {@link JenkinsConnection} with the given name.
    * @param connection the {@link JenkinsConnection} to disconnect.
    */
   public void disconnect( JenkinsConnection connection ) {
      verifyExists( connection );
      activator.disconnect( connection );
   }//End Method

   /**
    * Method to forget the {@link JenkinsConnection} with the given name.
    * @param connection the {@link JenkinsConnection} to forget.
    */
   public void forget( JenkinsConnection connection ) {
      verifyExists( connection );
      
      if ( activator.isActive( connection ) ) {
         activator.disconnect( connection );
      }
      
      connections.remove( connection );
   }//End Method

}//End Class
