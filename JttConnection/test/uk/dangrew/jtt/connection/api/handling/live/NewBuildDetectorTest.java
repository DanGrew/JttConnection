/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class NewBuildDetectorTest {

   private JenkinsJob job;
   private JenkinsJob anotherJob;
   
   private JenkinsDatabase database;
   private NewBuildDetector systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      job = new JenkinsJobImpl( "Job" );
      anotherJob = new JenkinsJobImpl( "Another" );
      database = new TestJenkinsDatabaseImpl();
      database.store( job );
      systemUnderTest = new NewBuildDetector( database );
   }//End Method

   @Test public void shouldProvideNewBuildWhenBuildNumberChangesOnExistingJob() {
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
      
      job.setBuildNumber( 23 );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList( job ) ) );
   }//End Method
   
   @Test public void shouldIgnoreResultStatusChange() {
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
      
      job.setBuildStatus( BuildResultStatus.UNSTABLE );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList() ) );
   }//End Method
   
   @Test public void shouldClearDetectedBuilds() {
      job.setBuildNumber( 23 );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList( job ) ) );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
   }//End Method
   
   @Test public void shouldProvideNewBuildWhenNewBuildFound() {
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
      
      database.store( anotherJob );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList( anotherJob ) ) );
   }//End Method
   
   @Test public void shouldProvideDetectedMultipleChanges() {
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
      
      job.setBuildNumber( 23 );
      database.store( anotherJob );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList( job, anotherJob ) ) );
   }//End Method
   
   @Test public void shouldNotDoubleUpChanges(){
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( new ArrayList<>() ) );
      
      job.setBuildNumber( 23 );
      job.setBuildNumber( 24 );
      assertThat( systemUnderTest.getAndClearNewBuilds(), is( Arrays.asList( job ) ) );
   }//End Method

}//End Class
