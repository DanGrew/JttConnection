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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.kode.event.structure.Event;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ConnectionManagerTest {

   private static final String LOCATION = "somewhere";
   private static final String USERNAME = "someone";
   private static final String PASSWORD = "something secret";
   
   @Mock private ConnectionEvent events;
   @Captor private ArgumentCaptor<Event< Pair< JenkinsConnection, ConnectionState > >> captor;
   
   @Mock private JenkinsConnection connection;
   @Mock private ConnectionActivator activator;
   private ConnectionManager systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      when( activator.makeConnection( LOCATION, USERNAME, PASSWORD ) ).thenReturn( connection );
      systemUnderTest = new ConnectionManager( events, activator );
   }//End Method
   
   @Test public void shouldMakeConnection(){
      assertThat( systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD ), is( connection ) );
   }//End Method
   
   @Test public void shouldNotMakeConnection(){
      when( activator.makeConnection( LOCATION, USERNAME, PASSWORD ) ).thenReturn( null );
      assertThat( systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD ), is( nullValue() ) );
   }//End Method

   @Test public void shouldConnect() {
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.connect( connection );
      verify( activator ).connect( connection );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowConnectThatDoesntExist() {
      systemUnderTest.connect( connection );
   }//End Method
   
   @Test public void shouldDisconnect() {
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.disconnect( connection );
      
      verify( activator ).disconnect( connection );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotDisconnectThatDoesntExist() {
      systemUnderTest.disconnect( connection );
   }//End Method
   
   @Test public void shouldForget() {
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.forget( connection );
      
      assertThat( systemUnderTest.connections().contains( connection ), is( false ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowForgetThatDoesntExist() {
      systemUnderTest.forget( connection );
   }//End Method
   
   @Test public void shouldProvideAllConnections(){
      assertThat( systemUnderTest.connections(), is( new ArrayList<>() ) );
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      assertThat( systemUnderTest.connections(), is( Arrays.asList( connection ) ) );
   }//End Method
   
   @Test public void shouldDisconnectIfConnectedWhenForgotten(){
      when( activator.isActive( connection ) ).thenReturn( true );
      
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.forget( connection );
      
      verify( activator ).disconnect( connection );
      assertThat( systemUnderTest.connections().contains( connection ), is( false ) );
   }//End Method
   
   @Test public void shouldNotifyNewConnection(){
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      verify( events ).fire( captor.capture() );
      
      assertThat( captor.getValue().getValue().getKey(), is( connection ) );
      assertThat( captor.getValue().getValue().getValue(), is( ConnectionState.Established ) );
   }//End Method
   
   @Test public void shouldNotifyConnectionMade(){
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.connect( connection );
      verify( events, times( 2 ) ).fire( captor.capture() );
      
      assertThat( captor.getValue().getValue().getKey(), is( connection ) );
      assertThat( captor.getValue().getValue().getValue(), is( ConnectionState.Connected ) );
   }//End Method

   @Test public void shouldNotifyDisconnection(){
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.disconnect( connection );
      verify( events, times( 2 ) ).fire( captor.capture() );
      
      assertThat( captor.getValue().getValue().getKey(), is( connection ) );
      assertThat( captor.getValue().getValue().getValue(), is( ConnectionState.Disconnected ) );
   }//End Method
   
   @Test public void shouldNotifyConnectionForgotten(){
      systemUnderTest.makeConnection( LOCATION, USERNAME, PASSWORD );
      systemUnderTest.forget( connection );
      verify( events, times( 2 ) ).fire( captor.capture() );
      
      assertThat( captor.getValue().getValue().getKey(), is( connection ) );
      assertThat( captor.getValue().getValue().getValue(), is( ConnectionState.Forgotten ) );
   }//End Method
   
   @Test public void shouldProvideWhetherActive(){
      when( activator.isActive( connection ) ).thenReturn( false );
      assertThat( systemUnderTest.isActive( connection ), is( false ) );
      
      when( activator.isActive( connection ) ).thenReturn( true );
      assertThat( systemUnderTest.isActive( connection ), is( true ) );
   }//End Method
}//End Constructor
