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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

public class DialogConfigurationTest {

   private Button button;
   
   private Dialog< String > dialog;
   private DialogPane pane;
   private ButtonType buttonType;
   @Mock private EventHandler< ActionEvent > handler;
   private DialogConfiguration systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
       JavaFxThreading.runAndWait( () -> dialog = new Dialog<>() );
      buttonType = ButtonType.APPLY;
      pane = dialog.getDialogPane();
      pane.getButtonTypes().add( buttonType );
      button = ( Button ) pane.lookupButton( buttonType );
      
      systemUnderTest = new DialogConfiguration();
   }//End Method
   
   @After public void validate() {
       Mockito.validateMockitoUsage();
   }//End Method

   @Test public void shouldAddEventFilterToButton() {
      systemUnderTest.provideFilterFor( pane, buttonType, ActionEvent.ACTION, handler );
      button.fire();
      verify( handler ).handle( Mockito.any() );
   }//End Method

}//End Class
