/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import java.util.ArrayList;
import java.util.List;

import uk.dangrew.jtt.model.commit.Commit;
import uk.dangrew.jtt.model.commit.CommitItem;
import uk.dangrew.jtt.model.commit.EditType;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * The {@link CommitModel} is used by the {@link CommitsParser} to push data into the model from
 * the api response.
 */
public class CommitModel {
   
   private final JenkinsDatabase database;
   
   private JenkinsJob job;
   
   private String commitId;
   private String message;
   private String comment;
   private Long timestamp;
   private JenkinsUser author;
   
   private String path;
   private EditType edit;
   private List< CommitItem > items;
   
   /**
    * Constructs a new {@link CommitModel}.
    */
   public CommitModel() {
      this( new SystemWideJenkinsDatabaseImpl().get() );
   }//End Constructor
   
   /**
    * Constructs a new {@link CommitModel}.
    * @param database the {@link JenkinsDatabase}.
    */
   CommitModel( JenkinsDatabase database ) {
      this.database = database;
      this.items = new ArrayList<>();
   }//End Constructor
   
   /**
    * Set the {@link JenkinsJob} to parse commits for.
    * @param job the {@link JenkinsJob} the commits should be pushed into.
    */
   void setJob( JenkinsJob job ) {
      this.job = job;
      this.job.commits().clear();
   }//End Method
 
   /**
    * Triggered with a change set has finished.
    * @param key the parse key.
    */
   void endChangeSet( String key ) {
      boolean invalid = commitId == null || timestamp == null || author == null;
      if ( !invalid ) {
         job.commits().add( new Commit( commitId, timestamp, author, comment, message, new ArrayList<>( items ) ) );
      }
      
      
      commitId = null;
      timestamp = null;
      author = null;
      comment = null;
      message = null;
      items.clear();
   }//End Method
   
   /**
    * Triggered with a commit path has finished.
    * @param key the parse key.
    */
   void endPath( String key ) {
      items.add( new CommitItem( path, edit ) );
   }//End Method
   
   /**
    * Sets the commit id.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitId( String key, String value ) {
      commitId = value;
   }//End Method
   
   /**
    * Sets the author by name.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitAuthor( String key, String value ) {
      author = database.getJenkinsUser( value );
      if ( author == null ) {
         author = new JenkinsUserImpl( value );
         database.store( author );
      }
   }//End Method
   
   /**
    * Sets the commit message.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitMessage( String key, String value ) {
      message = value;
   }//End Method
   
   /**
    * Sets the commit comment.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitComment( String key, String value ) {
      comment = value;
   }//End Method
   
   /**
    * Sets the commit timestamp.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitTimestamp( String key, Long value ) {
      timestamp = value;
   }//End Method
   
   /**
    * Sets the {@link EditType} for the particular commit item.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitEditType( String key, EditType value ) {
      edit = value;
   }//End Method
   
   /**
    * Sets the file changed for the particular commit item.
    * @param key the parse key.
    * @param value the value.
    */
   void setCommitFile( String key, String value ) {
      path = value;
   }//End Method
   
}//End Class