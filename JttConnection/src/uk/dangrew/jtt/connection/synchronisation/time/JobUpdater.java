/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.synchronisation.time;

import java.util.Timer;

import uk.dangrew.jtt.connection.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.synchronisation.model.TimeKeeper;

/**
 * The {@link JobUpdater} provides a {@link TimeKeeper} for updating the job details
 * from the {@link JenkinsApiImpl}.
 */
public class JobUpdater extends TimeKeeper {
   
   static final long UPDATE_DELAY = TimeKeeper.TASK_DELAY;
   static final long INTERVAL = 5000L;
   
   private final LiveStateFetcher fetcher;
   
   /**
    * Constructs a new {@link JobUpdater} with a default interval of 5 seconds.
    * @param api the {@link ExternalApi} to update for.
    * @param fetcher the {@link LiveStateFetcher} to request job updates on.
    */
   public JobUpdater( ExternalApi api, LiveStateFetcher fetcher ) {
      this( api, fetcher, new Timer(), INTERVAL );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobUpdater} with a default interval of 5 seconds.
    * @param api the {@link ExternalApi} to update for.
    */
   public JobUpdater( ExternalApi api ) {
      this( api, new LiveStateFetcher(), new Timer(), INTERVAL );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param api the {@link ExternalApi} to update for.
    * @param fetcher the {@link LiveStateFetcher} to request job updates on.
    * @param timer the {@link Timer} to time events.
    * @param interval the interval between updates.
    */
   public JobUpdater( ExternalApi api, LiveStateFetcher fetcher, Timer timer, Long interval ) {
      super( 
               () -> fetcher.updateBuildState( api ),
               timer, 
               interval
      );
      this.fetcher = fetcher;
   }//End Constructor
   
   /**
    * Method to determine whether the given is associated with this.
    * @param fetcher the {@link LiveStateFetcher} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( LiveStateFetcher fetcher ) {
      return this.fetcher == fetcher;
   }//End Method
}//End Class
