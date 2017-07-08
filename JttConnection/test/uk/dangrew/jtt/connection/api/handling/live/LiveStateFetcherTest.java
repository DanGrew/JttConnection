/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.connection.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsBaseRequest;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

@RunWith( JUnitParamsRunner.class )
public class LiveStateFetcherTest {
   
   private static final String API_RESPONSE = "The Api Response with lots of data.";
   
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   @Mock private JenkinsConnection connection;
   
   @Mock private ExternalApi api;
   @Mock private ApiResponseToJsonConverter converter;
   @Mock private JobDetailsParser parser;
   @Mock private CommitsFetcher commitsFetcher;
   @Mock private JenkinsFetcher fetcher;
   private JenkinsDatabase database;
   private LiveStateFetcher systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      //assume all valid parse requests
      when( converter.convert( Mockito.anyString() ) ).thenReturn( new JSONObject() );
      
      database = new TestJenkinsDatabaseImpl();
      job1 = new JenkinsJobImpl( "Job1" );
      job2 = new JenkinsJobImpl( "Job2" );
      job3 = new JenkinsJobImpl( "Job3" );
      database.store( job1 );
      database.store( job2 );
      database.store( job3 );
      
      systemUnderTest = new LiveStateFetcher( database, api, converter, parser, fetcher, commitsFetcher );
   }//End Method
   
   @Test public void shouldExecuteLastCompletedJobRequestAndParseIntoDatabase() {
      when( api.executeRequest( connection, JenkinsBaseRequest.LastCompleteJobDetailsRequest ) ).thenReturn( API_RESPONSE );
      JSONObject converted = new JSONObject();
      when( converter.convert( API_RESPONSE ) ).thenReturn( converted );
      
      systemUnderTest.loadLastCompletedBuild( connection );
      verify( parser ).parse( converted );
   }//End Method
   
   @Test public void shouldExecuteLastBuildJobRequestAndParseIntoDatabase() {
      when( api.executeRequest( connection, JenkinsBaseRequest.CurrentJobDetailsRequest ) ).thenReturn( API_RESPONSE );
      JSONObject converted = new JSONObject();
      when( converter.convert( API_RESPONSE ) ).thenReturn( converted );
      
      systemUnderTest.updateBuildState( connection );
      verify( parser ).parse( converted );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfStateHasntChanged(){
      systemUnderTest.updateBuildState( connection );
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfStateHasntChanged(){
      systemUnderTest.loadLastCompletedBuild( connection );
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfStateIsNotUnstable( BuildResultStatus status ){
      job1.setBuildStatus( status );
      job2.setBuildStatus( status );
      job3.setBuildStatus( status );
      
      systemUnderTest.updateBuildState( connection );
      if ( status == BuildResultStatus.UNSTABLE ) {
         verify( fetcher ).updateTestResults( connection, job1 );
         verify( fetcher ).updateTestResults( connection, job2 );
         verify( fetcher ).updateTestResults( connection, job3 );
      } else {
         verifyZeroInteractions( fetcher );
      }
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfStateIsNotUnstable( BuildResultStatus status ){
      job1.setBuildStatus( status );
      job2.setBuildStatus( status );
      job3.setBuildStatus( status );
      
      systemUnderTest.loadLastCompletedBuild( connection );
      if ( status == BuildResultStatus.UNSTABLE ) {
         verify( fetcher ).updateTestResults( connection, job1 );
         verify( fetcher ).updateTestResults( connection, job2 );
         verify( fetcher ).updateTestResults( connection, job3 );
      } else {
         verifyZeroInteractions( fetcher );
      }
   }//End Method
   
   @Test public void shouldUpdateTestsForJobWhenUpdatingBuildState(){
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      job3.setBuildStatus( BuildResultStatus.SUCCESS );
      
      systemUnderTest.updateBuildState( connection );
      job2.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState( connection );
      
      verify( fetcher ).updateTestResults( connection, job2 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsForJobWhenLoadingCompletedBuilds(){
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      job3.setBuildStatus( BuildResultStatus.SUCCESS );
      
      systemUnderTest.loadLastCompletedBuild( connection );
      job2.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild( connection );
      
      verify( fetcher ).updateTestResults( connection, job2 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfBuilding(){
      job1.buildStateProperty().set( BuildState.Building );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState( connection );
      
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfBuilding(){
      job1.buildStateProperty().set( BuildState.Building );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild( connection );
      
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsAgainForJobWhenUpdatingBuildState(){
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.updateBuildState( connection );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState( connection );
      
      verify( fetcher, times( 1 ) ).updateTestResults( connection, job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsAgainForJobWhenLoadingCompletedBuilds(){
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.loadLastCompletedBuild( connection );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild( connection );
      
      verify( fetcher, times( 1 ) ).updateTestResults( connection, job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsAgainForJobWhenUpdatingBuildStateIfBuildNumberHasChanged(){
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.updateBuildState( connection );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      job1.setBuildNumber( 23 );
      systemUnderTest.updateBuildState( connection );
      job1.setBuildNumber( new Integer( 23 ) );
      systemUnderTest.loadLastCompletedBuild( connection );
      
      verify( fetcher, times( 2 ) ).updateTestResults( connection, job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsAgainForJobWhenLoadingCompletedBuildsIfBuildNumberHasChanged(){
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.loadLastCompletedBuild( connection );
      job1.setBuildStatus( BuildResultStatus.UNSTABLE );
      job1.setBuildNumber( 23 );
      systemUnderTest.loadLastCompletedBuild( connection );
      job1.setBuildNumber( new Integer( 23 ) );
      systemUnderTest.loadLastCompletedBuild( connection );
      
      verify( fetcher, times( 2 ) ).updateTestResults( connection, job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotBreakUpdatingLoopIfConnectionToJenkinsLost(){
      when( converter.convert( Mockito.anyString() ) ).thenReturn( null );
      systemUnderTest.updateBuildState( connection );
      verify( parser, times( 0 ) ).parse( Mockito.any() );
      
      when( converter.convert( Mockito.anyString() ) ).thenReturn( new JSONObject() );
      systemUnderTest.updateBuildState( connection );
      verify( parser, times( 1 ) ).parse( Mockito.any() );
   }//End Method
   
   @Test public void shouldFetchCommitsAfterLastCompleteJobUpdate(){
      systemUnderTest.updateBuildState( connection );
      verify( commitsFetcher ).executeChangeSetsRequest( connection );
   }//End Method
   
   @Test public void shouldFetchCommitsAfterCurrentJobUpdate(){
      systemUnderTest.loadLastCompletedBuild( connection );
      verify( commitsFetcher ).executeChangeSetsRequest( connection );
   }//End Method
   
}//End Class
