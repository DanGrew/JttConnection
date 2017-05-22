/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

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
   }//End Method
   
   @Test public void shouldHavePadding() {
      assertThat( systemUnderTest.content().getHgap(), is( JenkinsLoginPrompt.PADDING ) );
      assertThat( systemUnderTest.content().getVgap(), is( JenkinsLoginPrompt.PADDING ) );
      assertThat( systemUnderTest.content().getPadding().getBottom(), is( JenkinsLoginPrompt.PADDING ) );
      assertThat( systemUnderTest.content().getPadding().getTop(), is( JenkinsLoginPrompt.PADDING ) );
      assertThat( systemUnderTest.content().getPadding().getLeft(), is( JenkinsLoginPrompt.PADDING ) );
      assertThat( systemUnderTest.content().getPadding().getRight(), is( JenkinsLoginPrompt.PADDING ) );
   }//End Method
   
   @Test public void shouldSplitIntoColumnsForAppearance(){
      assertThat( systemUnderTest.content().getColumnConstraints(), hasSize( 4 ) );
      assertThat( systemUnderTest.content().getColumnConstraints().get( 0 ).getPercentWidth(), is( JenkinsLoginPrompt.LEFT_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.content().getColumnConstraints().get( 1 ).getPercentWidth(), is( JenkinsLoginPrompt.LABEL_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.content().getColumnConstraints().get( 2 ).getPercentWidth(), is( JenkinsLoginPrompt.FIELD_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.content().getColumnConstraints().get( 3 ).getPercentWidth(), is( JenkinsLoginPrompt.RIGHT_COLUMN_PERCENT_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveLoginFieldsAndLabels() {
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.locationLabel() ), is( true ) );
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.usernameLabel() ), is( true ) );
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.passwordLabel() ), is( true ) );
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.locationTextField() ), is( true ) );
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.usernameTextField() ), is( true ) );
      assertThat( systemUnderTest.content().getChildren().contains( systemUnderTest.passwordTextField() ), is( true ) );
   }//End Method
   
   @Test public void shouldConfigureLoginFieldsAndLabels() {
      assertThat( systemUnderTest.locationLabel().getText(), is( JenkinsLoginPrompt.LOCATION_LABEL ) );
      assertThat( systemUnderTest.usernameLabel().getText(), is( JenkinsLoginPrompt.USERNAME_LABEL ) );
      assertThat( systemUnderTest.passwordLabel().getText(), is( JenkinsLoginPrompt.PASSWORD_LABEL ) );
      
      assertThat( systemUnderTest.locationTextField().getPromptText(), is( JenkinsLoginPrompt.LOCATION_PROMPT ) );
      assertThat( systemUnderTest.usernameTextField().getPromptText(), is( JenkinsLoginPrompt.USERNAME_PROMPT ) );
      assertThat( systemUnderTest.passwordTextField().getPromptText(), is( JenkinsLoginPrompt.PASSWORD_PROMPT ) );
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
      assertThat( filter.isMonitoringLocation( systemUnderTest.locationTextField() ), is( true ) );
      assertThat( filter.isMonitoringUsername( systemUnderTest.usernameTextField() ), is( true ) );
      assertThat( filter.isMonitoringPassword( systemUnderTest.passwordTextField() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideSuccessForLoginActionHavingPassedFilter() {
      assertThat( systemUnderTest.getResultConverter().call( systemUnderTest.loginButtonType() ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideFailureForCancel() {
      assertThat( systemUnderTest.getResultConverter().call( ButtonType.CANCEL ), is( false ) );
   }//End Method

}//End Class
