/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.sources;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JenkinsApiImpl} is responsible for connecting to Jenkins and logging in.
 */
public class JenkinsApiImpl implements ExternalApi {

   static final String PREEMPTIVE_AUTH = "preemptive-auth";
   static final int LOGIN_TIMEOUT = 5000;
   
   private final JenkinsApiRequests requests;
   private final ClientHandler clientHandler;
   private final JenkinsApiDigest digest;
   private final HttpContext getContext;
   
   /**
    * Constructs a new {@link JenkinsApiImpl}.
    */
   public JenkinsApiImpl() {
      this( new ClientHandler(), new JenkinsApiDigest(), JenkinsApiRequests.get() );
   }//End Constructor

   /**
    * Constructs a new {@link JenkinsApiImpl}.
    * @param clientHandler the {@link ClientHandler} used to handle interactions with jenkins.
    * @param digest the {@link JenkinsApiDigest} to use.
    * @param requests the {@link JenkinsApiRequests}.
    */
   JenkinsApiImpl( ClientHandler clientHandler, JenkinsApiDigest digest, JenkinsApiRequests requests ) {
      this.clientHandler = clientHandler;
      this.requests = requests;
      this.digest = digest;
      this.digest.attachSource( this );
      // Generate BASIC scheme object and stick it to the execution context
      BasicScheme basicAuth = new BasicScheme();
      getContext = new BasicHttpContext();
      getContext.setAttribute( PREEMPTIVE_AUTH, basicAuth );
   }//End Constructor

   /**
    * Method to determine whether the given {@link String} is a valid input.
    * @param input the {@link String} in question.
    * @return true if valid.
    */
   private boolean isInputValid( String input ) {
      return input != null && input.trim().length() > 0;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsConnection makeConnection( String location, String user, String password ) {
      if ( !isInputValid( location ) || !isInputValid( user ) || !isInputValid( password ) ) {
         digest.invalidInput();
         return null;
      }

      HttpClient client = clientHandler.constructClient( location, user, password );
      if ( client == null ) {
         return null;
      }
      
      String jenkinsLocation = requests.prefixJenkinsLocation( location );
      
      HttpGet get = requests.constructBaseRequest( jenkinsLocation );
      digest.executingLoginRequest();
      
      clientHandler.adjustClientTimeout( client.getParams(), LOGIN_TIMEOUT );
      String responseString = executeRequestAndUnpack( client, get );
      clientHandler.resetTimeout( client.getParams() );
      
      if ( responseString == null ) {
         digest.connectionFailed();
         return null;
      } else {
         digest.connectionSuccess();
         //needs to use name in some form, or remove name?
         return new JenkinsConnection( jenkinsLocation, user, password, client );
      }
   }//End Method
   
   /**
    * Convenience method to handle the execution of {@link HttpGet}s and unpack the responses
    * to a {@link String}.
    * @param client the {@link HttpClient} to execute with.
    * @param getRequest the {@link HttpGet} to execute.
    * @return the unpacked response, or null.
    */
   String executeRequestAndUnpack( HttpClient client, HttpGet getRequest ){
      try {
         HttpResponse response = client.execute( getRequest, getContext );
         digest.handlingResponse();
         String responseString = clientHandler.handleResponse( response );
         digest.responseReady();
         return responseString;
      } catch ( IOException exception ) {
         digest.connectionException( exception );
      }
      return null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String executeRequest( JenkinsConnection connection, JenkinsBaseRequest request ) {
      HttpGet get = request.execute( connection.location() );
      return executeRequestAndUnpack( connection.client(), get );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String executeRequest( JenkinsConnection connection, JobRequest request, JenkinsJob job ) {
      HttpGet get = request.execute( connection.location(), job );
      return executeRequestAndUnpack( connection.client(), get );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String executeRequest( JenkinsConnection connection, BuildRequest request, JenkinsJob job, int buildNumber ) {
      HttpGet get = request.execute( connection.location(), job, buildNumber );
      return executeRequestAndUnpack( connection.client(), get );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getJobsList( JenkinsConnection connection ) {
      HttpGet get = requests.constructJobListRequest( connection.location() );
      return executeRequestAndUnpack( connection.client(), get );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String getUsersList( JenkinsConnection connection ) {
      HttpGet get = requests.constructUserListRequest( connection.location() );
      return executeRequestAndUnpack( connection.client(), get );
   }//End Method

}//End Class
