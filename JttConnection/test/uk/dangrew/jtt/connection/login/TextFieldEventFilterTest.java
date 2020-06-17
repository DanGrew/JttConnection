/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import uk.dangrew.kode.launch.TestApplication;

public class TextFieldEventFilterTest {

   private TextField fieldA;
   private TextField fieldB;
   private TextField fieldC;
   
   @Mock private ActionEvent event;
   private TextFieldEventFilter systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      TestApplication.startPlatform();
      
      fieldA = new TextField( "Black" );
      fieldB = new TextField( "Stone" );
      fieldC = new TextField( "Cherry" );
      
      systemUnderTest = new TextFieldEventFilter( fieldA, fieldB, fieldC );
   }//End Method

   @Test public void shouldNotConsumeEventWhenAllFieldsPresent() {
      systemUnderTest.handle( event );
      verify( event, never() ).consume();
   }//End Method
   
   @Test public void shouldConsumeEventIfAnyFieldIsEmpty(){
      assertThatFieldIsValidated( fieldA, "   ", 1 );
      assertThatFieldIsValidated( fieldB, "   ", 2 );
      assertThatFieldIsValidated( fieldC, "   ", 3 );
   }//End Method
   
   /**
    * Method to assert that the {@link TextField} is handled when given certain text and the event is consumed.
    */
   private void assertThatFieldIsValidated( TextField field, String text, int consumeCount ) {
      String originalText = field.getText();
      field.setText( "   " );
      
      systemUnderTest.handle( event );
      verify( event, times( consumeCount ) ).consume();
      
      field.setText( originalText );
   }//End Method
   
   @Test public void shouldConsumeEventIfAnyFieldIsNull(){
      assertThatFieldIsValidated( fieldA, null, 1 );
      assertThatFieldIsValidated( fieldB, null, 2 );
      assertThatFieldIsValidated( fieldC, null, 3 );
   }//End Method
   
}//End Class
