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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.connection.javafx.dialog.DialogConfiguration;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JenkinsLoginPrompt} provides a {@link Dialog} for entering login details.
 */
public class JenkinsLoginPrompt extends Dialog< Boolean >{

   static final String TITLE = "Jenkins Test Tracker";
   static final String LOGIN = "Login";
   static final String HEADER_TEXT = "Welcome! Please log in.";
   static final String PASSWORD_LABEL = "Password:";
   static final String USERNAME_LABEL = "User Name:";
   static final String LOCATION_LABEL = "Jenkins Location:";
   static final String PASSWORD_PROMPT = "eg ofCourseThis1sntMyRealPassword";
   static final String USERNAME_PROMPT = "eg dang";
   static final String LOCATION_PROMPT = "eg 192.168.0.213:8089";
   static final double PADDING = 10;
   
   static final double LEFT_COLUMN_PERCENT_WIDTH = 10;
   static final double LABEL_COLUMN_PERCENT_WIDTH = 20;
   static final double FIELD_COLUMN_PERCENT_WIDTH = 60;
   static final double RIGHT_COLUMN_PERCENT_WIDTH = 10;
   
   private final GridPane content;
   private final Label locationLabel;
   private final Label usernameLabel;
   private final Label passwordLabel;
   private final TextField locationTextField;
   private final TextField usernameTextField;
   private final PasswordField passwordTextField;
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

      content = new GridPane();
      
      ColumnConstraints leftPadding = new ColumnConstraints();
      leftPadding.setPercentWidth( LEFT_COLUMN_PERCENT_WIDTH );
      content.getColumnConstraints().add( leftPadding );
      
      ColumnConstraints labelConstraint = new ColumnConstraints();
      labelConstraint.setPercentWidth( LABEL_COLUMN_PERCENT_WIDTH );
      content.getColumnConstraints().add( labelConstraint );
      
      ColumnConstraints fieldConstraint = new ColumnConstraints();
      fieldConstraint.setPercentWidth( FIELD_COLUMN_PERCENT_WIDTH );
      content.getColumnConstraints().add( fieldConstraint );
      
      ColumnConstraints rightPadding = new ColumnConstraints();
      rightPadding.setPercentWidth( RIGHT_COLUMN_PERCENT_WIDTH );
      content.getColumnConstraints().add( rightPadding );
      
      content.setAlignment( Pos.CENTER );
      content.setHgap( PADDING );
      content.setVgap( PADDING );
      content.setPadding( new Insets( PADDING ) );

      locationTextField = new TextField();
      locationTextField.setPromptText( LOCATION_PROMPT );
      usernameTextField = new TextField();
      usernameTextField.setPromptText( USERNAME_PROMPT );
      passwordTextField = new PasswordField();
      passwordTextField.setPromptText( PASSWORD_PROMPT );

      content.add( locationLabel = new Label( LOCATION_LABEL ), 1, 0 );
      content.add( locationTextField, 2, 0 );
      content.add( usernameLabel = new Label( USERNAME_LABEL ), 1, 1 );
      content.add( usernameTextField, 2, 1 );
      content.add( passwordLabel = new Label( PASSWORD_LABEL ), 1, 2 );
      content.add( passwordTextField, 2, 2 );

      BorderPane border = new BorderPane( content );
      border.setBottom( digestViewer );
      getDialogPane().setContent( border );
      
      digest.attachSource( this );
      JenkinsCredentialEventFilter loginFilter = new JenkinsCredentialEventFilter( 
               digest, locationTextField, usernameTextField, passwordTextField 
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
   
   GridPane content(){
      return content;
   }//End Method
   
   ButtonType loginButtonType(){
      return loginButtonType;
   }//End Method
   
   Label locationLabel(){
      return locationLabel;
   }//End Method
   
   Label usernameLabel(){
      return usernameLabel;
   }//End Method
   
   Label passwordLabel(){
      return passwordLabel;
   }//End Method
   
   TextField locationTextField(){
      return locationTextField;
   }//End Method
   
   TextField usernameTextField(){
      return usernameTextField;
   }//End Method
   
   PasswordField passwordTextField(){
      return passwordTextField;
   }//End Method

}// End Class
