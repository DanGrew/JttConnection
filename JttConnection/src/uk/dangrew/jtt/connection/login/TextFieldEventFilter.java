/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * The {@link TextFieldEventFilter} provides an {@link EventHandler} for filtering button presses
 * on a {@link javafx.scene.control.Dialog} by consuming the event if input is invalid.
 */
public class TextFieldEventFilter implements EventHandler< ActionEvent > {

   private final Set< TextField > fields;
   
   /**
    * Constructs a new {@link TextFieldEventFilter}.
    * @param fields the {@link TextField} to validate.
    */
   public TextFieldEventFilter( TextField... fields ) {
      this.fields = new LinkedHashSet<>( Arrays.asList( fields ) );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void handle( ActionEvent event ) {
      for ( TextField field : fields ) {
         String fieldText = field.getText();
         if ( fieldText == null || fieldText.trim().length() == 0 ) {
            event.consume();
            return;
         }
      }
   }//End Method

}//End Class
