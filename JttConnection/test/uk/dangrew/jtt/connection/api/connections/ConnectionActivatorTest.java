/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.connection.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.synchronisation.time.JobUpdater;

public class ConnectionActivatorTest {

   @Mock private JenkinsConnection connection;
   @Mock private JobUpdater updater;
   
   @Mock private LiveStateFetcher fetcher;
   private ConnectionActivator systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      systemUnderTest = new ConnectionActivator( fetcher );
   }//End Method

   @Test public void shouldConnectApiAndStartJobUpdater() {
      assertThat( systemUnderTest.updaterFor( connection ), is( nullValue() ) );
      
      systemUnderTest.connect( connection );
      
      verify( fetcher ).loadLastCompletedBuild( connection );
      
      JobUpdater updater = systemUnderTest.updaterFor( connection );
      assertThat( updater.isAssociatedWith( fetcher ), is( true ) );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotConnectApiIfAlreadyConnected() {
      systemUnderTest.connect( connection );
      systemUnderTest.connect( connection );
   }//End Method
   
   @Test public void shouldDisconnectApiAndStopJobUpdater() {
      systemUnderTest.connect( connection );
      
      JobUpdater updater = systemUnderTest.updaterFor( connection );
      assertThat( updater, is( notNullValue() ) );
      assertThat( updater.hasTimer(), is( true ) );
      
      systemUnderTest.disconnect( connection );
      assertThat( updater.hasTimer(), is( false ) );
      assertThat( systemUnderTest.updaterFor( connection ), is( nullValue() ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotStopApiIfNotConnected() {
      systemUnderTest.disconnect( connection );
   }//End Method
   
   @Test public void shouldProvideActiveStateBasedOnJobUpdater() {
      assertThat( systemUnderTest.isActive( connection ), is( false ) );
      systemUnderTest.connect( connection );
      assertThat( systemUnderTest.isActive( connection ), is( true ) );
      systemUnderTest.disconnect( connection );
      assertThat( systemUnderTest.isActive( connection ), is( false ) );
   }//End Method
   
}//End Class
