/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import uk.dangrew.jtt.connection.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.connection.api.handling.JenkinsFetcherImpl;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.connection.api.sources.JenkinsBaseRequest;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * {@link LiveStateFetcher} provides a method of updating the live information of {@link JenkinsJob}s. */
public class LiveStateFetcher {

   private final ExternalApi api;
   private final JenkinsDatabase database;
   private final ApiResponseToJsonConverter converter;
   private final JobDetailsParser jobsParser;
   private final JenkinsFetcher fetcher;
   private final CommitsFetcher commitsFetcher;
   private final Map< JenkinsJob, Integer > unstableJobsLastBuildNumbersTestRetrievedFor;
   
   /**
    * Constructs a new {@link LiveStateFetcher}.
    */
   public LiveStateFetcher() {
      this( 
               new SystemWideJenkinsDatabaseImpl().get(), 
               new JenkinsApiImpl(),
               new ApiResponseToJsonConverter(), 
               new JobDetailsParser( new JobDetailsModel() ),
               new JenkinsFetcherImpl(),
               new CommitsFetcher()
      );
   }//End Constructor

   /**
    * Constructs a new {@link LiveStateFetcher}.
    * @param database the {@link JenkinsDatabase} to populate and update.
    * @param api the {@link ExternalApi} to execute requests.
    * @param converter the {@link ApiResponseToJsonConverter}.
    * @param jobParser the {@link JobDetailsParser}.
    * @param fetcher the {@link JenkinsFetcher}. 
    * @param commitsFetcher the {@link CommitsFetcher}.
    */
   LiveStateFetcher( 
            JenkinsDatabase database, 
            ExternalApi api,
            ApiResponseToJsonConverter converter,
            JobDetailsParser jobParser,
            JenkinsFetcher fetcher,
            CommitsFetcher commitsFetcher
   ) {
      if ( database == null ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      
      this.database = database;
      this.api = api;
      this.converter = converter;
      this.jobsParser = jobParser;
      this.fetcher = fetcher;
      this.commitsFetcher = commitsFetcher;
      
      this.unstableJobsLastBuildNumbersTestRetrievedFor = new HashMap<>();
   }//End Constructor

   /**
    * Method to load all job details for the last completed build of each {@link JenkinsJob}.
    * @param connection the {@link JenkinsConnection} to execute with.
    */
   public void loadLastCompletedBuild( JenkinsConnection connection ) {
      updateState( connection, JenkinsBaseRequest.LastCompleteJobDetailsRequest );
   }//End Method
   
   /**
    * Method to update the current build state of each {@link JenkinsJob}.
    * @param connection the {@link JenkinsConnection} to execute with.
    */
   public void updateBuildState( JenkinsConnection connection ) {
      updateState( connection, JenkinsBaseRequest.CurrentJobDetailsRequest );
   }//End Method
   
   /**
    * Method to update the details given the {@link JenkinsBaseRequest} to execute.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param request the {@link JenkinsBaseRequest} to execute.
    */
   private void updateState( JenkinsConnection connection, JenkinsBaseRequest request ) {
      String response = api.executeRequest( connection, request );
      
      JSONObject converted = converter.convert( response );
      if ( converted == null ) {
         return;
      }
      jobsParser.parse( converted );
      
      database.jenkinsJobs().forEach( j -> detectAndRequestTestResultUpdates( connection, j ) );
      
      commitsFetcher.executeChangeSetsRequest( connection );
   }//End Method
   
   /**
    * Method to detect where test results are required and to perform the update immediately.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param job the {@link JenkinsJob} to check.
    */
   private void detectAndRequestTestResultUpdates( JenkinsConnection connection, JenkinsJob job ){
      if ( job.buildStateProperty().get() == BuildState.Building ) {
         return;
      }
      BuildResultStatus current = job.getBuildStatus();
      if ( current != BuildResultStatus.UNSTABLE ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.remove( job );
         return;
      }
      
      Integer lastNumberUpdatedFor = unstableJobsLastBuildNumbersTestRetrievedFor.get( job );
      if ( lastNumberUpdatedFor == null ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.put( job, job.getBuildNumber() );
         fetcher.updateTestResults( connection, job );
      } else if ( !lastNumberUpdatedFor.equals( job.getBuildNumber() ) ) {
         unstableJobsLastBuildNumbersTestRetrievedFor.put( job, job.getBuildNumber() );
         fetcher.updateTestResults( connection, job );
      }
   }//End Method

}//End Class
