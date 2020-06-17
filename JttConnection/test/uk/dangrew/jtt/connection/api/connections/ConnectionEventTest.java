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
import uk.dangrew.kode.event.structure.AbstractEventManagerTest;
import uk.dangrew.kode.event.structure.EventManager;

public class ConnectionEventTest extends AbstractEventManagerTest< Pair< JenkinsConnection, ConnectionState > > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< Pair< JenkinsConnection, ConnectionState > > constructSut() {
      return new ConnectionEvent();
   }//End Method

}//End Class
