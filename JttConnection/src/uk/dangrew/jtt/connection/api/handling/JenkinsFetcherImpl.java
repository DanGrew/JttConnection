/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling;

import static uk.dangrew.jtt.connection.api.sources.JobRequest.LastBuildTestResultsUnwrappedRequest;
import static uk.dangrew.jtt.connection.api.sources.JobRequest.LastBuildTestResultsWrappedRequest;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.data.json.tests.JsonTestResultsImporter;
import uk.dangrew.jtt.connection.data.json.tests.JsonTestResultsImporterImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * {@link JenkinsFetcherImpl} provides an implementation of the {@link JenkinsFetcher} interface
 * for updating {@link JenkinsJob}s.
 */
//no test!?
public class JenkinsFetcherImpl implements JenkinsFetcher {

   private final JsonTestResultsImporter testsImporter;
   private final JenkinsFetcherDigest digest;
   
   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    */
   public JenkinsFetcherImpl() {
      this( new SystemWideJenkinsDatabaseImpl().get(), new JenkinsFetcherDigest() );
   }//End Constructor

   /**
    * Constructs a new {@link JenkinsFetcherImpl}.
    * @param database the {@link JenkinsDatabase} to populate and update.
    * @param digest the {@link JenkinsFetcherDigest} to use.
    */
   JenkinsFetcherImpl( JenkinsDatabase database, JenkinsFetcherDigest digest ) {
      if ( database == null ) {
         throw new IllegalArgumentException( "Null database provided." );
      }
      
      this.testsImporter = new JsonTestResultsImporterImpl( database );
      
      this.digest = digest;
      this.digest.attachSource( this );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( ExternalApi api, JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) {
         return;
      }
      
      String response = api.executeRequest( LastBuildTestResultsWrappedRequest, jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
      response = api.executeRequest( LastBuildTestResultsUnwrappedRequest, jenkinsJob );
      testsImporter.updateTestResults( jenkinsJob, response );
   }//End Method

}//End Class
