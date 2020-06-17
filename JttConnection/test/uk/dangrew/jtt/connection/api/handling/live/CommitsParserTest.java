/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Test;

import uk.dangrew.jtt.model.commit.Commit;
import uk.dangrew.jtt.model.commit.EditType;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.utility.io.IoCommon;

public class CommitsParserTest {

   @Test public void shouldReadFileWithCommits(){
      String parsed = new IoCommon().readFileIntoString( getClass(), "commits.txt" );
      JSONObject object = new JSONObject( parsed );
      
      JenkinsJob job = new JenkinsJobImpl( "anything" );
      
      CommitModel model = new CommitModel();
      model.setJob( job );
      new CommitsParser( model ).parse( object );
      
      assertThat( job.commits(), hasSize( 18 ) );
      
      Commit firstCommit = job.commits().get( 0 );
      assertThat( firstCommit.id(), is( "cd07928a65a5e5b23b7ad6ad5e97efbab197c990" ) );
      assertThat( firstCommit.timestamp(), is( 1496348140000L ) );
      assertThat( firstCommit.user().nameProperty().get(), is( "noreply" ) );
      assertThat( firstCommit.comment(), is( "1.6.30 RELEASE\n" ) );
      assertThat( firstCommit.message(), is( "1.6.30 RELEASE" ) );
      
      Commit secondCommit = job.commits().get( 1 );
      assertThat( secondCommit.id(), is( "0b648e810818f08bfdca94e93f47c8dd4bd802f5" ) );
      assertThat( secondCommit.timestamp(), is( 1496519900000L ) );
      assertThat( secondCommit.user().nameProperty().get(), is( "danielanthonygrew" ) );
      assertThat( secondCommit.comment(), is( "WALL BUILDER AND CONTENT AREA:\nInitial versions with area resizing correctly and wall builder providing \nsplit functionality.\n" ) );
      assertThat( secondCommit.message(), is( "WALL BUILDER AND CONTENT AREA:" ) );
      
      assertThat( secondCommit.commits(), hasSize( 5 ) );
      assertThat( secondCommit.commits().get( 0 ).itemPath(), is( "JttDesktop/src/uk/dangrew/jtt/desktop/wallbuilder/ContentArea.java" ) );
      assertThat( secondCommit.commits().get( 0 ).editType(), is( EditType.edit ) );
      assertThat( secondCommit.commits().get( 1 ).itemPath(), is( "JttDesktop/test/uk/dangrew/jtt/desktop/wallbuilder/ContentAreaTest.java" ) );
      assertThat( secondCommit.commits().get( 1 ).editType(), is( EditType.add ) );
      assertThat( secondCommit.commits().get( 2 ).itemPath(), is( "JttDesktop/src/uk/dangrew/jtt/desktop/wallbuilder/WallBuilder.java" ) );        
      assertThat( secondCommit.commits().get( 2 ).editType(), is( EditType.edit ) );
      assertThat( secondCommit.commits().get( 3 ).itemPath(), is( "JttDesktop/test/uk/dangrew/jtt/desktop/wallbuilder/ContentAreaAsserter.java" ) );
      assertThat( secondCommit.commits().get( 3 ).editType(), is( EditType.delete ) );
      assertThat( secondCommit.commits().get( 4 ).itemPath(), is( "JttDesktop/test/uk/dangrew/jtt/desktop/wallbuilder/WallBuilderTest.java" ) ); 
      assertThat( secondCommit.commits().get( 4 ).editType(), is( EditType.edit ) );
      
      assertThat( firstCommit.commits(), hasSize( 1 ) );
      assertThat( firstCommit.commits().get( 0 ).itemPath(), is( "RELEASES" ) );
      assertThat( firstCommit.commits().get( 0 ).editType(), is( EditType.edit ) );
   }//End Method

}//End Class
