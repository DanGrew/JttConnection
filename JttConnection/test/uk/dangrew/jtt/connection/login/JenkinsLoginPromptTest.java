/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.sun.javafx.application.PlatformImpl;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.connection.javafx.dialog.DialogConfiguration;
import uk.dangrew.sd.graphics.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JenkinsLoginPromptTest {

   @Spy private DialogConfiguration configuration;
   private DigestViewer digestViewer;
   @Spy private JenkinsLoginDigest digest;
   private JenkinsLoginPrompt systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      TestApplication.startPlatform();
      digestViewer = new DigestViewer();
      PlatformImpl.runAndWait( () -> systemUnderTest = new JenkinsLoginPrompt(
               configuration, digest, digestViewer
      ) );
   }//End Method

   @Ignore
   @Test public void manual() {
      TestApplication.startPlatform();
      
      PlatformImpl.runAndWait( () -> {
         new JenkinsLoginPrompt( digestViewer ).showAndWait();
      } );
   }//End Method
   
   @Test public void shouldAttachSourceToDigest(){
      verify( digest ).attachSource( systemUnderTest );
   }//End Method
   
   @Test public void shouldHaveTitleAndHeader() {
      assertThat( systemUnderTest.getTitle(), is( JenkinsLoginPrompt.TITLE ) );
      assertThat( systemUnderTest.getHeaderText(), is( JenkinsLoginPrompt.HEADER_TEXT ) );
   }//End Method
   
   @Test public void shouldBeResizable() {
      assertThat( systemUnderTest.isResizable(), is( true ) );
   }//End Method
   
   @Test public void shouldProvideLoginCancelButtonTypes() {
      assertThat( systemUnderTest.getDialogPane().getButtonTypes(), hasSize( 2 ) );
      assertThat( systemUnderTest.getDialogPane().getButtonTypes().get( 0 ).getText(), is( JenkinsLoginPrompt.LOGIN ) );
      assertThat( systemUnderTest.getDialogPane().getButtonTypes().get( 0 ).getButtonData(), is( ButtonData.OK_DONE ) );
      assertThat( systemUnderTest.getDialogPane().getButtonTypes().get( 1 ), is( ButtonType.CANCEL ) );
   }//End Method
   
   @Test public void shouldHaveContent() {
      BorderPane wrapper = ( BorderPane )systemUnderTest.getDialogPane().getContent();
      assertThat( wrapper.getCenter(), is( systemUnderTest.content() ) );
      assertThat( systemUnderTest.content().getAlignment(), is( Pos.CENTER ) );
      assertThat( systemUnderTest.content(), is( instanceOf( JenkinsLoginDetails.class ) ) );
   }//End Method
   
   @Test public void shouldPlaceDigestInWithContent(){
      BorderPane border = ( BorderPane )systemUnderTest.getDialogPane().getContent();
      assertThat( border.getBottom(), is( digestViewer ) );
   }//End Method
   
   @Test public void shouldProvideFilterOnLoginButton(){
      ArgumentCaptor< JenkinsCredentialEventFilter > filterCaptor = ArgumentCaptor.forClass( JenkinsCredentialEventFilter.class );
      verify( configuration ).provideFilterFor( 
               eq( systemUnderTest.getDialogPane() ), 
               eq( systemUnderTest.loginButtonType() ),
               eq( ActionEvent.ACTION ),
               filterCaptor.capture()
      );
      
      JenkinsCredentialEventFilter filter = filterCaptor.getValue();
      assertThat( filter.isAssociatedWith( digest ), is( true ) );
      assertThat( filter.isMonitoringLocation( systemUnderTest.content().locationField() ), is( true ) );
      assertThat( filter.isMonitoringUsername( systemUnderTest.content().usernameField() ), is( true ) );
      assertThat( filter.isMonitoringPassword( systemUnderTest.content().passwordField() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideSuccessForLoginActionHavingPassedFilter() {
      assertThat( systemUnderTest.getResultConverter().call( systemUnderTest.loginButtonType() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideFailureForCancel() {
      assertThat( systemUnderTest.getResultConverter().call( ButtonType.CANCEL ), is( false ) );
   }//End Method

}//End Class
