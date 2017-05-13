/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.javafx.dialog;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class DialogConfigurationTest {

   @Mock private Button button;
   
   private DialogPane pane;
   private ButtonType buttonType;
   @Mock private EventHandler< ActionEvent > handler;
   private DialogConfiguration systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      buttonType = ButtonType.APPLY;
      pane = new DialogPane();
      pane.getButtonTypes().add( buttonType );
      
      PlatformImpl.runAndWait( () -> systemUnderTest = new DialogConfiguration() );
   }//End Method

   @Test public void shouldAddEventFilterToButton() {
      systemUnderTest.provideFilterFor( pane, buttonType, ActionEvent.ACTION, handler );
      verify( button ).addEventFilter( ActionEvent.ACTION, handler );
   }//End Method

}//End Class
