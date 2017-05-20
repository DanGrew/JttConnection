/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import java.util.HashMap;
import java.util.Map;

import uk.dangrew.jtt.connection.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.connection.synchronisation.time.JobUpdater;

/**
 * The {@link ConnectionActivator} is responsible for activating and managing a 
 * connection to an {@link uk.dangrew.jtt.connection.api.sources.ExternalApi}.
 */
public class ConnectionActivator {
   
   private final LiveStateFetcher fetcher;
   private final Map< JenkinsConnection, JobUpdater > activations;
   
   /**
    * Constructs a new {@link ConnectionActivator}.
    */
   public ConnectionActivator() {
      this( new LiveStateFetcher() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ConnectionActivator}.
    * @param fetcher the {@link LiveStateFetcher} for fetching updates. 
    */
   ConnectionActivator( LiveStateFetcher fetcher ) {
      this.fetcher = fetcher;
      this.activations = new HashMap<>();
   }//End Constructor
   
   /**
    * Method to connect the given {@link JenkinsConnection}, trigger the polling of data.
    * @param connection the {@link JenkinsConnection} to connect. Must not already be connected.
    */
   public void connect( JenkinsConnection connection ) {
      if ( activations.containsKey( connection ) ) {
         throw new IllegalArgumentException( "Connection already active: " + connection.name() );
      }
      
      fetcher.loadLastCompletedBuild( connection.api() );
      JobUpdater updater = new JobUpdater( connection.api(), fetcher );
      activations.put( connection, updater );
   }//End Method

   /**
    * Method to disconnect the given {@link JenkinsConnection}, terminating the polling.
    * @param connection the {@link JenkinsConnection} to terminate. Must be already connected.
    */
   public void disconnect( JenkinsConnection connection ) {
      if ( !activations.containsKey( connection ) ) {
         throw new IllegalArgumentException( "Connection not active: " + connection.name() );
      }
      
      activations.get( connection ).shutdown();
      activations.remove( connection );
   }//End Method

   /**
    * Method to determine whether the given {@link JenkinsConnection} is active, ie polling.
    * @param connection the {@link JenkinsConnection} in question.
    * @return true if polling, false if dormant, false if not present.
    */
   public boolean isActive( JenkinsConnection connection ) {
      if ( !activations.containsKey( connection ) ) {
         return false;
      }
      
      return activations.get( connection ).hasTimer();
   }//End Method

   /**
    * Access to the associated {@link JobUpdater} for the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} in question.
    * @return the associated {@link JobUpdater}.
    */
   JobUpdater updaterFor( JenkinsConnection connection ) {
      return activations.get( connection );
   }//End Method

}//End Class
