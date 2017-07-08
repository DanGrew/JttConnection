/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import uk.dangrew.jtt.model.commit.EditType;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayWithObjectParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.EnumParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.LongParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;

/**
 * The {@link CommitsParser} provides a {@link JsonParser} for parsing the jenkins
 * response containing the commits detected by a particular build.
 */
public class CommitsParser extends JsonParser {
   
   private static final String ARRAY_CHANGE_ITEMS = "items";
   private static final String ARRAY_PATHS = "paths";
   
   private static final String KEY_COMMIT_ID = "commitId";
   private static final String KEY_MESSAGE = "msg";
   private static final String KEY_COMMENT = "comment";
   private static final String KEY_EDIT_TYPE = "editType";
   private static final String KEY_FILE = "file";
   
   private static final String KEY_TIMESTAMP = "timestamp";
   private static final String KEY_FULL_NAME = "fullName";
   
   /**
    * Constructs a new {@link CommitsParser}.
    * @param commitModel the {@link CommitModel}.
    */
   public CommitsParser( CommitModel commitModel ) {
      when( ARRAY_CHANGE_ITEMS, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               null, commitModel::endChangeSet, null, null 
      ) ) );
      
      when( ARRAY_PATHS, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               null, commitModel::endPath, null, null 
      ) ) );
      
      when( KEY_FULL_NAME, new StringParseHandle( commitModel::setCommitAuthor ) );
      when( KEY_COMMIT_ID, new StringParseHandle( commitModel::setCommitId ) );
      when( KEY_MESSAGE, new StringParseHandle( commitModel::setCommitMessage ) );
      when( KEY_COMMENT, new StringParseHandle( commitModel::setCommitComment ) );
      when( KEY_EDIT_TYPE, new EnumParseHandle<>( EditType.class, commitModel::setCommitEditType ) );
      when( KEY_FILE, new StringParseHandle( commitModel::setCommitFile ) );
      
      when( KEY_TIMESTAMP, new LongParseHandle( commitModel::setCommitTimestamp ) );
   }//End Constructor
   
}//End Class
