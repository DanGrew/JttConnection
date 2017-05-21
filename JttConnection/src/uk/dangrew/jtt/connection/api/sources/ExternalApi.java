/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.sources;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link ExternalApi} provides an interface to an external source of data
 * that provides information for {@link JenkinsJob}s.
 */
public interface ExternalApi {

   /**
    * Method make a connection with the given credentials.
    * @param jenkinsLocation the location of Jenkins.
    * @param user the user name.
    * @param password the password.
    * @return the {@link JenkinsConnection} that has been checked and is successful, null otherwise.
    */
   public JenkinsConnection makeConnection( String jenkinsLocation, String user, String password );
   
   /**
    * Method to execute the given {@link JenkinsBaseRequest}.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param request the {@link JenkinsBaseRequest} to execute.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( JenkinsConnection connection, JenkinsBaseRequest request );
   
   /**
    * Method to execute the given {@link JobRequest} against the given {@link JenkinsJob}.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param request the {@link JobRequest} to execute.
    * @param job the {@link JenkinsJob} the request is for.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( JenkinsConnection connection, JobRequest request, JenkinsJob job );
   
   /**
    * Method to execute the given {@link BuildRequest} against the given {@link JenkinsJob}.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @param request the {@link BuildRequest} to execute.
    * @param job the {@link JenkinsJob} the request is for.
    * @param buildNumber the build number the request is for.
    * @return the {@link String} response from the api.
    */
   public String executeRequest( JenkinsConnection connection, BuildRequest request, JenkinsJob job, int buildNumber );
   
   /**
    * Method to get the list of job names currently available.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @return the {@link String} response from the api.
    */
   public String getJobsList( JenkinsConnection connection );
   
   /**
    * Method to get the list of users currently available.
    * @param connection the {@link JenkinsConnection} to execute with.
    * @return the {@link String} response from the api.
    */
   public String getUsersList( JenkinsConnection connection );

}//End Interface
