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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;

public class JenkinsConnectionTest {

   private static final String NAME = "name";
   private static final String LOCATION = "location";
   private static final String USER = "user";
   private static final String PASS = "pass";
   
   @Mock private ExternalApi api;
   private JenkinsConnection systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JenkinsConnection( 
               NAME, LOCATION, USER, PASS, api 
      );
   }//End Method

   @Test public void shouldProvideName() {
      assertThat( systemUnderTest.name(), is( NAME ) );
   }//End Method
   
   @Test public void shouldProvideLocation() {
      assertThat( systemUnderTest.location(), is( LOCATION ) );
   }//End Method
   
   @Test public void shouldProvideUsername() {
      assertThat( systemUnderTest.username(), is( USER ) );
   }//End Method
   
   @Test public void shouldProvidePassword() {
      assertThat( systemUnderTest.password(), is( PASS ) );
   }//End Method
   
   @Test public void shouldProvideApi(){
      assertThat( systemUnderTest.api(), is( api ) );
   }//End Method

}//End Class
