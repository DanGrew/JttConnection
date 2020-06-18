/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.api.sources.JobRequest;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jupa.json.io.ApiResponseToJsonConverter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CommitsFetcherTest {

   @Mock private JenkinsConnection connection;
   private JSONObject jsonObject;
   private String response;
   
   @Mock private ApiResponseToJsonConverter converter;
   @Mock private ExternalApi api;
   @Mock private NewBuildDetector newBuildDetector;
   
   @Mock private CommitModel model;
   @Mock private CommitsParser parser;
   private CommitsFetcher systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      jsonObject = new JSONObject();
      response = "anything";
      systemUnderTest = new CommitsFetcher( converter, api, newBuildDetector, model, parser );
   }//End Method

   @Test public void shouldRequestChangeSetsForNewBuildsFollowingUpdate(){
      when( api.executeRequest( Mockito.eq( connection ), Mockito.eq( JobRequest.ChangeSetsRequest ), Mockito.any() ) ).thenReturn( response );
      when( converter.convert( response ) ).thenReturn( jsonObject );
     
      List< JenkinsJob > newJobs = Arrays.asList( 
               new JenkinsJobImpl( "First" ), 
               new JenkinsJobImpl( "Second" ),
               new JenkinsJobImpl( "third" )
      );
      when( newBuildDetector.getAndClearNewBuilds() ).thenReturn( newJobs );
      
      systemUnderTest.executeChangeSetsRequest( connection );
      for ( JenkinsJob job : newJobs ) {
         verify( model ).setJob( job );
         verify( api ).executeRequest( connection, JobRequest.ChangeSetsRequest, job );
      }
      verify( parser, times( newJobs.size() ) ).parse( jsonObject );
   }//End Method
   
   @Test public void shouldIgnoreNoResponseForChangeSets(){
      when( api.executeRequest( Mockito.eq( connection ), Mockito.eq( JobRequest.ChangeSetsRequest ), Mockito.any() ) ).thenReturn( null );
      when( converter.convert( response ) ).thenReturn( jsonObject );
     
      List< JenkinsJob > newJobs = Arrays.asList( 
               new JenkinsJobImpl( "First" ), 
               new JenkinsJobImpl( "Second" ),
               new JenkinsJobImpl( "third" )
      );
      when( newBuildDetector.getAndClearNewBuilds() ).thenReturn( newJobs );
      
      systemUnderTest.executeChangeSetsRequest( connection );
      for ( JenkinsJob job : newJobs ) {
         verify( model ).setJob( job );
         verify( api ).executeRequest( connection, JobRequest.ChangeSetsRequest, job );
      }
      verifyZeroInteractions( parser );
   }//End Method
   
   @Test public void shouldIgnoreUnparseableResponseForChangeSets(){
      when( api.executeRequest( Mockito.eq( connection ), Mockito.eq( JobRequest.ChangeSetsRequest ), Mockito.any() ) ).thenReturn( response );
      when( converter.convert( response ) ).thenReturn( null );
     
      List< JenkinsJob > newJobs = Arrays.asList( 
               new JenkinsJobImpl( "First" ), 
               new JenkinsJobImpl( "Second" ),
               new JenkinsJobImpl( "third" )
      );
      when( newBuildDetector.getAndClearNewBuilds() ).thenReturn( newJobs );
      
      systemUnderTest.executeChangeSetsRequest( connection );
      for ( JenkinsJob job : newJobs ) {
         verify( model ).setJob( job );
         verify( api ).executeRequest( connection, JobRequest.ChangeSetsRequest, job );
      }
      verifyZeroInteractions( parser );
   }//End Method

}//End Class
