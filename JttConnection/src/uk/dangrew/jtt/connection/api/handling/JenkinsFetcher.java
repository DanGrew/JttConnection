/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling;

import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JenkinsFetcher} describes an object that can retrieve updated information
 * from an {@link ExternalApi} and update {@link JenkinsJob}s with responses.
 */
public interface JenkinsFetcher {

   /**
    * Method to fetch the latest test results from the latest build of the given {@link JenkinsJob}.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param jenkinsJob the {@link JenkinsJob} to get results for.
    */
   public void updateTestResults( JenkinsConnection connection, JenkinsJob jenkinsJob );
   
}//End Interface
