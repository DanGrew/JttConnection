/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import javafx.util.Pair;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.kode.event.structure.EventManager;
import uk.dangrew.kode.event.structure.EventSubscription;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ConnectionEvent} provides an {@link EventManager} for {@link JenkinsConnection} {@link ConnectionState} changes.
 */
public class ConnectionEvent extends EventManager< Pair< JenkinsConnection, ConnectionState > > {

   private static final Collection<EventSubscription< Pair< JenkinsConnection, ConnectionState > >> subscriptions =
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link ConnectionEvent}.
    */
   public ConnectionEvent() {
      super( subscriptions, lock );
   }//End Constructor

}//End Constructor
