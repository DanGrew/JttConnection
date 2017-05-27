/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.connection.javafx.dialog.DialogConfiguration;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JenkinsLoginPrompt} provides a {@link Dialog} for entering login details.
 */
public class JenkinsLoginPrompt extends Dialog< Boolean >{

   static final String TITLE = "Jenkins Test Tracker";
   static final String LOGIN = "Login";
   static final String HEADER_TEXT = "Welcome! Please log in.";

   private final JenkinsLoginDetails content;
   private final ButtonType loginButtonType;

   /**
    * Constructs a new {@link JenkinsLoginPrompt}.
    * @param digestViewer the {@link DigestViewer} for displaying the digest.
    */
   public JenkinsLoginPrompt( DigestViewer digestViewer ) {
      this( 
               new DialogConfiguration(), 
               new JenkinsLoginDigest(),
               digestViewer
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsLoginPrompt}.
    * @param configuration the {@link DialogConfiguration}.
    * @param digest the {@link JenkinsLoginDigest} for reporting.
    * @param digestViewer the {@link DigestViewer} for displaying the digest.
    */
   JenkinsLoginPrompt( DialogConfiguration configuration, JenkinsLoginDigest digest, DigestViewer digestViewer ) {
      setTitle( TITLE );
      setHeaderText( HEADER_TEXT );
      setResizable( true );

      loginButtonType = new ButtonType( LOGIN, ButtonData.OK_DONE );
      getDialogPane().getButtonTypes().addAll( loginButtonType, ButtonType.CANCEL );

      content = new JenkinsLoginDetails();
      
      BorderPane border = new BorderPane( content );
      border.setBottom( digestViewer );
      getDialogPane().setContent( border );
      
      digest.attachSource( this );
      JenkinsCredentialEventFilter loginFilter = new JenkinsCredentialEventFilter( 
               digest, content.locationField(), content.usernameField(), content.passwordField() 
      );
      configuration.provideFilterFor( getDialogPane(), loginButtonType, ActionEvent.ACTION, loginFilter );
      setResultConverter( button -> button == loginButtonType );
   }// End Constructor
   
   /**
    * {@link #showAndWait()} that isn't final.
    */
   public Optional< Boolean > friendly_showAndWait(){
      //not tested - friendly to workaround final - do not change
      return showAndWait();
   }//End Method
   
   JenkinsLoginDetails content(){
      return content;
   }//End Method
   
   ButtonType loginButtonType(){
      return loginButtonType;
   }//End Method
   
   JenkinsLoginDetails loginDetails(){
      return content;
   }//End Method
   
}// End Class
