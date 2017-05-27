/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

/**
 * {@link ConnectionState} provides the state of a {@link uk.dangrew.jtt.connection.api.sources.JenkinsConnection}.
 */
public enum ConnectionState {

   Established,
   Connected,
   Disconnected,
   Forgotten
   
}//End Enum
