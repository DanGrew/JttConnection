/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ConnectionManagerTest {

   private static final String NAME = "name";
   
   @Mock private JenkinsConnection connection;
   @Mock private ConnectionActivator activator;
   private ConnectionManager systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      when( connection.name() ).thenReturn( NAME );
      systemUnderTest = new ConnectionManager( activator );
   }//End Method

   @Test public void shouldStoreNewConnection() {
      assertThat( systemUnderTest.get( NAME ), is( nullValue() ) );
      systemUnderTest.store( connection );
      assertThat( systemUnderTest.get( NAME ), is( connection ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowNewConnectionWithDuplicateName() {
      systemUnderTest.store( connection );
      systemUnderTest.store( connection );
   }//End Method
   
   @Test public void shouldMakeConnection() {
      systemUnderTest.store( connection );
      systemUnderTest.connect( NAME );
      
      verify( activator ).connect( connection );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowConnectThatDoesntExist() {
      systemUnderTest.connect( NAME );
   }//End Method
   
   @Test public void shouldDisconnect() {
      systemUnderTest.store( connection );
      systemUnderTest.disconnect( NAME );
      
      verify( activator ).disconnect( connection );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotDisconnectThatDoesntExist() {
      systemUnderTest.disconnect( NAME );
   }//End Method
   
   @Test public void shouldForget() {
      systemUnderTest.store( connection );
      systemUnderTest.forget( NAME );
      
      assertThat( systemUnderTest.get( NAME ), is( nullValue() ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowForgetThatDoesntExist() {
      systemUnderTest.forget( NAME );
   }//End Method
   
   @Test public void shouldProvideAllConnections(){
      assertThat( systemUnderTest.connections(), is( new ArrayList<>() ) );
      systemUnderTest.store( connection );
      assertThat( systemUnderTest.connections(), is( Arrays.asList( connection ) ) );
   }//End Method
   
   @Test public void shouldSortConnectionsByName(){
      JenkinsConnection second = mock( JenkinsConnection.class );
      when( second.name() ).thenReturn( "anything-before-name" );
      
      systemUnderTest.store( connection );
      systemUnderTest.store( second );
      
      assertThat( systemUnderTest.connections(), is( Arrays.asList( second, connection ) ) );
   }//End Method
   
   @Test public void shouldDisconnectIfConnectedWhenForgotten(){
      when( activator.isActive( connection ) ).thenReturn( true );
      
      systemUnderTest.store( connection );
      systemUnderTest.forget( NAME );
      
      verify( activator ).disconnect( connection );
      assertThat( systemUnderTest.get( NAME ), is( nullValue() ) );
   }//End Method

}//End Constructor
