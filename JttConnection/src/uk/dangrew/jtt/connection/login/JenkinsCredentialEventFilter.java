/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.login;

import org.apache.http.client.HttpClient;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;

/**
 * The {@link JenkinsCredentialEventFilter} provides an {@link EventHandler} for filtering login
 * requests given credentials by attempting to login with them.
 */
public class JenkinsCredentialEventFilter implements EventHandler< ActionEvent >{

   private final ExternalApi api;
   private final JenkinsLoginDigest digest;
   private final TextFieldEventFilter inputFilter;
   private final TextField location;
   private final TextField username;
   private final TextField password;
   
   /**
    * Constructs a new {@link JenkinsCredentialEventFilter}.
    * @param api the {@link ExternalApi} to login with.
    * @param digest the {@link JenkinsLoginDigest} to report progress.
    * @param locationField the {@link TextField} for the location.
    * @param usernameField the {@link TextField} for the username.
    * @param passwordField the {@link TextField} for the password.
    */
   public JenkinsCredentialEventFilter( ExternalApi api, JenkinsLoginDigest digest, TextField locationField, TextField usernameField, TextField passwordField ) {
      this( 
               new TextFieldEventFilter( locationField, usernameField, passwordField ),
               api, 
               digest,
               locationField, usernameField, passwordField 
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsCredentialEventFilter}.
    * @param inputFilter the {@link TextFieldEventFilter} for validating inputs.
    * @param api the {@link ExternalApi} to login with.
    * @param digest the {@link JenkinsLoginDigest} to report progress.
    * @param locationField the {@link TextField} for the location.
    * @param usernameField the {@link TextField} for the username.
    * @param passwordField the {@link TextField} for the password.
    */
   JenkinsCredentialEventFilter( 
            TextFieldEventFilter inputFilter, 
            ExternalApi api, 
            JenkinsLoginDigest digest,
            TextField locationField, 
            TextField usernameField, 
            TextField passwordField 
   ) {
      this.inputFilter = inputFilter;
      this.api = api;
      this.digest = digest;
      this.location = locationField;
      this.username = usernameField;
      this.password = passwordField;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void handle( ActionEvent event ) {
      inputFilter.handle( event );
      
      if ( event.isConsumed() ) {
         digest.validationError();
         return;
      }
      digest.acceptCredentials();
      
      HttpClient client = api.attemptLogin( location.getText(), username.getText(), password.getText() );
      if ( client == null ) {
         digest.loginFailed();
         event.consume();
      }
      
      digest.loginSuccessful();
   }//End Method

   /**
    * Method to determine whether the given is associated with this object.
    * @param api the {@link ExternalApi} in question.
    * @return true if identical to the {@link ExternalApi} used by this object.
    */
   boolean isAssociatedWith( ExternalApi api ) {
      return this.api == api;
   }//End Method
   
   /**
    * Method to determine whether the given is associated with this object.
    * @param digest the {@link JenkinsLoginDigest} in question.
    * @return true if identical to the {@link JenkinsLoginDigest} used by this object.
    */
   boolean isAssociatedWith( JenkinsLoginDigest digest ) {
      return this.digest == digest;
   }//End Method

   /**
    * Method to determine whether the given is being monitored by this object.
    * @param field the {@link TextField} in question.
    * @return true if identical to the {@link TextField} used by this object.
    */
   boolean isMonitoringLocation( TextField field ) {
      return this.location == field;
   }//End Method
   
   /**
    * Method to determine whether the given is being monitored by this object.
    * @param field the {@link TextField} in question.
    * @return true if identical to the {@link TextField} used by this object.
    */
   boolean isMonitoringUsername( TextField field ) {
      return this.username == field;
   }//End Method
   
   /**
    * Method to determine whether the given is being monitored by this object.
    * @param field the {@link TextField} in question.
    * @return true if identical to the {@link TextField} used by this object.
    */
   boolean isMonitoringPassword( TextField field ) {
      return this.password == field;
   }//End Method

}//End Class
