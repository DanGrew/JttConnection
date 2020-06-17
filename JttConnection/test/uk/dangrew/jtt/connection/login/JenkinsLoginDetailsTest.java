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

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.kode.launch.TestApplication;

public class JenkinsLoginDetailsTest {

   private JenkinsLoginDetails systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      systemUnderTest = new JenkinsLoginDetails();
   }//End Method

   @Test public void shouldHavePadding() {
      assertThat( systemUnderTest.getHgap(), is( JenkinsLoginDetails.PADDING ) );
      assertThat( systemUnderTest.getVgap(), is( JenkinsLoginDetails.PADDING ) );
      assertThat( systemUnderTest.getPadding().getBottom(), is( JenkinsLoginDetails.PADDING ) );
      assertThat( systemUnderTest.getPadding().getTop(), is( JenkinsLoginDetails.PADDING ) );
      assertThat( systemUnderTest.getPadding().getLeft(), is( JenkinsLoginDetails.PADDING ) );
      assertThat( systemUnderTest.getPadding().getRight(), is( JenkinsLoginDetails.PADDING ) );
   }//End Method
   
   @Test public void shouldSplitIntoColumnsForAppearance(){
      assertThat( systemUnderTest.getColumnConstraints(), hasSize( 4 ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth(), is( JenkinsLoginDetails.LEFT_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 1 ).getPercentWidth(), is( JenkinsLoginDetails.LABEL_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 2 ).getPercentWidth(), is( JenkinsLoginDetails.FIELD_COLUMN_PERCENT_WIDTH ) );
      assertThat( systemUnderTest.getColumnConstraints().get( 3 ).getPercentWidth(), is( JenkinsLoginDetails.RIGHT_COLUMN_PERCENT_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveLoginFieldsAndLabels() {
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.locationLabel() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.usernameLabel() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.passwordLabel() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.locationField() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.usernameField() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.passwordField() ), is( true ) );
   }//End Method
   
   @Test public void shouldConfigureLoginFieldsAndLabels() {
      assertThat( systemUnderTest.locationLabel().getText(), is( JenkinsLoginDetails.LOCATION_LABEL ) );
      assertThat( systemUnderTest.usernameLabel().getText(), is( JenkinsLoginDetails.USERNAME_LABEL ) );
      assertThat( systemUnderTest.passwordLabel().getText(), is( JenkinsLoginDetails.PASSWORD_LABEL ) );
      
      assertThat( systemUnderTest.locationField().getPromptText(), is( JenkinsLoginDetails.LOCATION_PROMPT ) );
      assertThat( systemUnderTest.usernameField().getPromptText(), is( JenkinsLoginDetails.USERNAME_PROMPT ) );
      assertThat( systemUnderTest.passwordField().getPromptText(), is( JenkinsLoginDetails.PASSWORD_PROMPT ) );
   }//End Method

}//End Class
