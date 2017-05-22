/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.connection.login.JenkinsLoginPrompt;
import uk.dangrew.sd.graphics.launch.TestApplication;
import uk.dangrew.sd.viewer.basic.DigestViewer;

public class JenkinsApiConnectionPromptTest {

   @Mock private DigestViewer digestViewer;
   @Mock private JenkinsLoginPrompt prompt;
   @Mock private Function< DigestViewer, JenkinsLoginPrompt > promptSupplier;
   private JenkinsApiConnectionPrompt systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      when( promptSupplier.apply( digestViewer ) ).thenReturn( prompt );
      systemUnderTest = new JenkinsApiConnectionPrompt( 
               promptSupplier
      );
   }//End Method

   @Test public void shouldReturnIfCantLogin() {
      when( prompt.friendly_showAndWait() ).thenReturn( Optional.of( false ) );
      assertThat( systemUnderTest.connect( digestViewer ), is( false ) );
   }// End Method
   
   @Test public void shouldReturnApiIfLoggedIn() {
      when( prompt.friendly_showAndWait() ).thenReturn( Optional.of( true ) );
      assertThat( systemUnderTest.connect( digestViewer ), is( true ) );
   }// End Method
   
}//End Class
