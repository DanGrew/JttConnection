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

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.api.sources.JobRequest;
import uk.dangrew.jtt.connection.data.json.conversion.ApiResponseToJsonConverter;

/**
 * The {@link CommitsFetcher} is responsible for requesting the change sets for the new builds
 * from the {@link JenkinsApiImpl}.
 */
class CommitsFetcher {
   
   private final CommitModel commitModel;
   private final CommitsParser commitsParser;
   private final NewBuildDetector newBuildDetector;
   
   private final ExternalApi api;
   private final ApiResponseToJsonConverter converter;
   
   /**
    * Constructs a new {@link CommitsFetcher}.
    */
   CommitsFetcher() {
      this( new ApiResponseToJsonConverter(), new JenkinsApiImpl(), new NewBuildDetector(), new CommitModel() );
   }//End Class
   
   /**
    * Constructs a new {@link CommitsFetcher}.
    * @param converter the {@link ApiResponseToJsonConverter}.
    * @param api the {@link JenkinsApiImpl}.
    * @param newBuildDetector the {@link NewBuildDetector}.
    * @param commitModel the {@link CommitModel}.
    */
   private CommitsFetcher(
            ApiResponseToJsonConverter converter, 
            ExternalApi api, 
            NewBuildDetector newBuildDetector, 
            CommitModel commitModel
   ) {
      this( 
               converter,
               api,
               newBuildDetector,
               commitModel,
               new CommitsParser( commitModel )
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link CommitsFetcher}.
    * @param converter the {@link ApiResponseToJsonConverter}.
    * @param api the {@link JenkinsApiImpl}.
    * @param newBuildDetector the {@link NewBuildDetector}.
    * @param commitModel the {@link CommitModel}.
    * @param commitsParser the {@link CommitsParser}.
    */
   CommitsFetcher( 
            ApiResponseToJsonConverter converter, 
            ExternalApi api, 
            NewBuildDetector newBuildDetector, 
            CommitModel commitModel,
            CommitsParser commitsParser 
   ) {
      this.converter = converter;
      this.api = api;
      this.newBuildDetector = newBuildDetector;
      
      this.commitModel = commitModel;
      this.commitsParser = commitsParser;
   }//End Class
   
   /**
    * Method to execute the change sets request on the new builds for the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} to execute on.
    */
   void executeChangeSetsRequest( JenkinsConnection connection ){
      newBuildDetector.getAndClearNewBuilds().forEach( j -> {
         commitModel.setJob( j );
         
         String response = api.executeRequest( connection, JobRequest.ChangeSetsRequest, j );
         JSONObject converted = converter.convert( response );
         if ( converted == null ) {
            return;
         }
         commitsParser.parse( converted );
      } );
   }//End Method

}//End Class
