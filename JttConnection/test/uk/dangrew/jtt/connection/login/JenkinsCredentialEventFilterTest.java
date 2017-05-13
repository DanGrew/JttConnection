/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class JenkinsCredentialEventFilterTest {
   
   private TextField location;
   private TextField username;
   private TextField password;
   
   @Mock private ActionEvent event;

   @Mock private ExternalApi api; 
   @Mock private JenkinsLoginDigest digest;
   @Mock private TextFieldEventFilter inputFilter;
   private JenkinsCredentialEventFilter systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      TestApplication.startPlatform();
      
      location = new TextField( "Black" );
      username = new TextField( "Stone" );
      password = new TextField( "Cherry" );
      
      systemUnderTest = new JenkinsCredentialEventFilter( inputFilter, api, digest, location, username, password );
   }//End Method
   
   @Test public void shouldNotAttemptLoginIfInputInvalid() {
      when( event.isConsumed() ).thenReturn( true );
      systemUnderTest.handle( event );
      verify( api, never() ).attemptLogin( Mockito.anyString(), Mockito.anyString(), Mockito.anyString() );
      verify( digest ).validationError();
   }//End Method

   @Test public void shouldAttemptLoginIfInputValid(){
      when( event.isConsumed() ).thenReturn( false );
      systemUnderTest.handle( event );
      verify( api ).attemptLogin( location.getText(), username.getText(), password.getText() );
      verify( digest ).acceptCredentials();
   }//End Method
   
   @Test public void shouldConsumeEventIfLoginFails(){
      when( event.isConsumed() ).thenReturn( false );
      when( api.attemptLogin( location.getText(), username.getText(), password.getText() ) ).thenReturn( null );
      
      systemUnderTest.handle( event );
      verify( event ).consume();
      
      verify( digest ).loginFailed();
   }//End Method
   
   @Test public void shouldNotConsumeEventIfLoginSucceeds(){
      when( event.isConsumed() ).thenReturn( false );
      when( api.attemptLogin( location.getText(), username.getText(), password.getText() ) ).thenReturn( mock( HttpClient.class ) );
      
      systemUnderTest.handle( event );
      verify( event, never() ).consume();
      
      verify( digest ).loginSuccessful();
   }//End Method
   
   @Test public void shouldBeAssociatedWithApi(){
      assertThat( systemUnderTest.isAssociatedWith( api ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( mock( ExternalApi.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithDigest(){
      assertThat( systemUnderTest.isAssociatedWith( digest ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( mock( JenkinsLoginDigest.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldBeMonitoringFields(){
      assertThat( systemUnderTest.isMonitoringLocation( location ), is( true ) );
      assertThat( systemUnderTest.isMonitoringLocation( new TextField() ), is( false ) );
      
      assertThat( systemUnderTest.isMonitoringUsername( username ), is( true ) );
      assertThat( systemUnderTest.isMonitoringUsername( new TextField() ), is( false ) );
      
      assertThat( systemUnderTest.isMonitoringPassword( password ), is( true ) );
      assertThat( systemUnderTest.isMonitoringPassword( new TextField() ), is( false ) );
   }//End Method
}//End Constructor
