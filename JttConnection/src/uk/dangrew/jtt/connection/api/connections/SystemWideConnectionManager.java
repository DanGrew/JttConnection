/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import uk.dangrew.jtt.model.storage.database.SystemWideObject;

/**
 * {@link SystemWideObject} for the {@link ConnectionManager}.
 */
public class SystemWideConnectionManager extends SystemWideObject< ConnectionManager > {

   private static final ConnectionManager connectionManager = new ConnectionManager();
   
   /**
    * Constructs a new {@link SystemWideConnectionManager}.
    */
   public SystemWideConnectionManager() {
      super( connectionManager );
   }//End Constructor

}//End Class
