/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.commit.Commit;
import uk.dangrew.jtt.model.commit.CommitItem;
import uk.dangrew.jtt.model.commit.EditType;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class CommitModelTest {
   
   private static final String KEY = "anything";
   private static final String COMMIT_ID = "ldsiuvjalkjsnd";
   private static final Long TIMESTAMP = 48765384L;
   private static final String AUTHOR = "Dan";
   private static final String COMMENT = "a comment";
   private static final String MESSAGE = "a message";
   private static final List< CommitItem > COMMITS = Arrays.asList(
            new CommitItem( "here", EditType.add ),
            new CommitItem( "there", EditType.delete ),
            new CommitItem( "everywhere", EditType.edit )
   );
   
   private JenkinsJob job;
   private JenkinsDatabase database;
   private CommitModel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      job = new JenkinsJobImpl( "Job" );
      job.commits().add( mock( Commit.class ) );
      
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new CommitModel( database );
      systemUnderTest.setJob( job );
   }//End Method

   @Test public void shouldClearCommitsWhenJobSet() {
      assertThat( job.commits(), is( empty() ) );
   }//End Method
   
   @Test public void shouldCreateCommitWhenChangeSetComplete() {
      parseBasicCommit();
      
      assertThat( job.commits(), hasSize( 1 ) );
      Commit commit = job.commits().get( 0 );
      assertThat( commit.id(), is( COMMIT_ID ) );
      assertThat( commit.timestamp(), is( TIMESTAMP ) );
      assertThat( commit.comment(), is( COMMENT ) );
      assertThat( commit.message(), is( MESSAGE ) );
      assertThat( commit.user().nameProperty().get(), is( AUTHOR ) );
      
      for ( int i = 0; i < COMMITS.size(); i++ ) {
         assertThat( commit.commits().get( i ).editType(), is( COMMITS.get( i ).editType() ) );
         assertThat( commit.commits().get( i ).itemPath(), is( COMMITS.get( i ).itemPath() ) );
      }
   }//End Method
   
   @Test public void shouldClearBetweenChangeSetsAndIgnoreEmptyData() {
      parseBasicCommit();
      
      assertThat( job.commits(), hasSize( 1 ) );
      systemUnderTest.endChangeSet( KEY );
      assertThat( job.commits(), hasSize( 1 ) );
   }//End Method
   
   @Test public void shouldCreateNewUserIfAuthorNotPresentInDatabase() {
      assertThat( database.getJenkinsUser( AUTHOR ), is( nullValue() ) );
      parseBasicCommit();
      assertThat( database.getJenkinsUser( AUTHOR ), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldClearCommitItemsBetweenChangeSets(){
      parseBasicCommit();
      
      systemUnderTest.setCommitId( KEY, COMMIT_ID );
      systemUnderTest.setCommitTimestamp( KEY, TIMESTAMP );
      systemUnderTest.setCommitComment( KEY, COMMENT );
      systemUnderTest.setCommitMessage( KEY, MESSAGE );
      systemUnderTest.setCommitAuthor( KEY, AUTHOR );
      
      systemUnderTest.setCommitFile( KEY, "somewhere" );
      systemUnderTest.setCommitEditType( KEY, EditType.edit );
      systemUnderTest.endPath( KEY );
      
      systemUnderTest.endChangeSet( KEY );
      
      assertThat( job.commits(), hasSize( 2 ) );
      assertThat( job.commits().get( 0 ).commits(), hasSize( COMMITS.size() ) );
      assertThat( job.commits().get( 1 ).commits(), hasSize( 1 ) );
   }//End Method
   
   /**
    * Method to perform a basic parse with all data present, defined at the top of this test.
    */
   private void parseBasicCommit(){
      systemUnderTest.setCommitId( KEY, COMMIT_ID );
      systemUnderTest.setCommitTimestamp( KEY, TIMESTAMP );
      systemUnderTest.setCommitComment( KEY, COMMENT );
      systemUnderTest.setCommitMessage( KEY, MESSAGE );
      systemUnderTest.setCommitAuthor( KEY, AUTHOR );
      
      for ( int i = 0; i < COMMITS.size(); i++ ) {
         systemUnderTest.setCommitFile( KEY, COMMITS.get( i ).itemPath() );
         systemUnderTest.setCommitEditType( KEY, COMMITS.get( i ).editType() );
         systemUnderTest.endPath( KEY );
      }
      
      systemUnderTest.endChangeSet( KEY );
   }//End Method
}//End Class
