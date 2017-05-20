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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The {@link ConnectionManager} is responsible for managing the {@link JenkinsConnection}s in the system
 * and access to them.
 */
public class ConnectionManager {

   private final ConnectionActivator activator;
   private final Map< String, JenkinsConnection > connections;
   
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
      this.connections = new TreeMap<>();
      this.activator = activator;
   }//End Constructor
   
   /**
    * Getter for the {@link JenkinsConnection} of the given name.
    * @param name the name of the {@link JenkinsConnection}.
    * @return the {@link JenkinsConnection} with the given name.
    */
   public JenkinsConnection get( String name ) {
      return connections.get( name );
   }//End Method
   
   /**
    * Method to verify there is no duplicate {@link JenkinsConnection} name.
    * @param name the name to check.
    */
   private void verifyNoDuplicate( String name ) {
      if ( get( name ) != null ) {
         throw new IllegalArgumentException( "Connection already exists: " + name );
      }
   }//End Method
   
   /**
    * Method to verufy that a {@link JenkinsConnection} exists for the given name.
    * @param name the name in question.
    */
   private void verifyExists( String name ) {
      if ( get( name ) == null ) {
         throw new IllegalArgumentException( "Connection does not exist: " + name );
      }
   }//End Method
   
   /**
    * Access to the {@link JenkinsConnection}s present, but not necessarily active.
    * @return a {@link List} of {@link JenkinsConnection}s.
    */
   public List< JenkinsConnection > connections() {
      return new ArrayList<>( connections.values() );
   }//End Method
   
   /**
    * Method to store the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} to store. Must not be already present.
    */
   public void store( JenkinsConnection connection ) {
      verifyNoDuplicate( connection.name() );
      
      connections.put( connection.name(), connection );
   }//End Method

   /**
    * Method to connect the {@link JenkinsConnection} with the given name.
    * @param name the name of the {@link JenkinsConnection}. {@link JenkinsConnection} must be present
    * for this name,
    */
   public void connect( String name ) {
      verifyExists( name );
      activator.connect( connections.get( name ) );
   }//End Method

   /**
    * Method to disconnect the {@link JenkinsConnection} with the given name.
    * @param name the name of the {@link JenkinsConnection}. {@link JenkinsConnection} must be present
    * for this name,
    */
   public void disconnect( String name ) {
      verifyExists( name );
      activator.disconnect( connections.get( name ) );
   }//End Method

   /**
    * Method to forget the {@link JenkinsConnection} with the given name.
    * @param name the name of the {@link JenkinsConnection}. {@link JenkinsConnection} must be present
    * for this name.
    */
   public void forget( String name ) {
      verifyExists( name );
      
      JenkinsConnection connection = get( name );
      if ( activator.isActive( connection ) ) {
         activator.disconnect( connection );
      }
      
      connections.remove( name );
   }//End Method

}//End Class
