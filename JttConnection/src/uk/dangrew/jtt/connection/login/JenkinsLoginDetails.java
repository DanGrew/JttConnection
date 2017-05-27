/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * {@link JenkinsLoginDetails} provides a basic panel with {@link Label}s and {@link TextField}s
 * for the details of a jenkins login.
 */
public class JenkinsLoginDetails extends GridPane {

   static final String PASSWORD_LABEL = "Password:";
   static final String USERNAME_LABEL = "User Name:";
   static final String LOCATION_LABEL = "Jenkins Location:";
   static final String PASSWORD_PROMPT = "eg ofCourseThis1sntMyRealPassword";
   static final String USERNAME_PROMPT = "eg dang";
   static final String LOCATION_PROMPT = "eg 192.168.0.213:8089";
   
   static final double LEFT_COLUMN_PERCENT_WIDTH = 10;
   static final double LABEL_COLUMN_PERCENT_WIDTH = 30;
   static final double FIELD_COLUMN_PERCENT_WIDTH = 50;
   static final double RIGHT_COLUMN_PERCENT_WIDTH = 10;
   static final double PADDING = 10;
   
   private final Label locationLabel;
   private final Label usernameLabel;
   private final Label passwordLabel;
   private final TextField locationTextField;
   private final TextField usernameTextField;
   private final PasswordField passwordTextField;
   
   /**
    * Constructs a new {@link JenkinsLoginDetails}.
    */
   public JenkinsLoginDetails() {
      ColumnConstraints leftPadding = new ColumnConstraints();
      leftPadding.setPercentWidth( LEFT_COLUMN_PERCENT_WIDTH );
      getColumnConstraints().add( leftPadding );
      
      ColumnConstraints labelConstraint = new ColumnConstraints();
      labelConstraint.setPercentWidth( LABEL_COLUMN_PERCENT_WIDTH );
      getColumnConstraints().add( labelConstraint );
      
      ColumnConstraints fieldConstraint = new ColumnConstraints();
      fieldConstraint.setPercentWidth( FIELD_COLUMN_PERCENT_WIDTH );
      getColumnConstraints().add( fieldConstraint );
      
      ColumnConstraints rightPadding = new ColumnConstraints();
      rightPadding.setPercentWidth( RIGHT_COLUMN_PERCENT_WIDTH );
      getColumnConstraints().add( rightPadding );
      
      setAlignment( Pos.CENTER );
      setHgap( PADDING );
      setVgap( PADDING );
      setPadding( new Insets( PADDING ) );

      locationTextField = new TextField();
      locationTextField.setPromptText( LOCATION_PROMPT );
      usernameTextField = new TextField();
      usernameTextField.setPromptText( USERNAME_PROMPT );
      passwordTextField = new PasswordField();
      passwordTextField.setPromptText( PASSWORD_PROMPT );

      add( locationLabel = new Label( LOCATION_LABEL ), 1, 0 );
      add( locationTextField, 2, 0 );
      add( usernameLabel = new Label( USERNAME_LABEL ), 1, 1 );
      add( usernameTextField, 2, 1 );
      add( passwordLabel = new Label( PASSWORD_LABEL ), 1, 2 );
      add( passwordTextField, 2, 2 );
   }//End Constructor
   
   /**
    * Access to the location {@link TextField}.
    * @return the {@link TextField}.
    */
   public TextField locationField(){
      return locationTextField;
   }//End Method
   
   /**
    * Access to the username {@link TextField}.
    * @return the {@link TextField}.
    */
   public TextField usernameField(){
      return usernameTextField;
   }//End Method
   
   /**
    * Access to the password {@link PasswordField}.
    * @return the {@link PasswordField}.
    */
   public PasswordField passwordField(){
      return passwordTextField;
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
}//End Class
