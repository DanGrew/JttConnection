/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.javafx.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/**
 * The {@link DialogConfiguration} provides som decoupling of common functions for setting up {@link javafx.scene.control.Dialog}s.
 */
public class DialogConfiguration {

   /**
    * Method to provide an event filter on the given {@link DialogPane} for a {@link ButtonType}.
    * @param dialogPane the {@link DialogPane}.
    * @param buttonType the {@link ButtonType} associated with the {@link Button}.
    * @param eventType the {@link EventType}.
    * @param handler the {@link EventHandler} to handle the event.
    */
   public void provideFilterFor( 
            DialogPane dialogPane, 
            ButtonType buttonType, 
            EventType< ActionEvent > eventType, 
            EventHandler< ActionEvent > handler 
   ) {
      Button loginButton = ( Button )dialogPane.lookupButton( buttonType );
      loginButton.addEventFilter(
          eventType, 
          handler
      );
   }//End Method
   
}//End Class
