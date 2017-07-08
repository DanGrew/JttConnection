/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.handling.live;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link NewBuildDetector} is responsible for identifying new builds in the form of new {@link JenkinsJob}s
 * and a change in the build number of existing jobs.
 */
public class NewBuildDetector {
   
   private final Set< JenkinsJob > newBuilds;
   
   /**
    * Constructs a new {@link NewBuildDetector}.
    */
   public NewBuildDetector() {
      this( new SystemWideJenkinsDatabaseImpl().get() );
   }//End Constructor
   
   /**
    * Constructs a new {@link NewBuildDetector}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   NewBuildDetector( JenkinsDatabase database ) {
      this.newBuilds = new LinkedHashSet<>();
      
      database.jenkinsJobProperties().addBuildResultStatusListener( ( s, o , u ) -> {
         if ( o.getKey().intValue() != u.getKey().intValue() ) {
            newBuilds.add( s );
         }
      } );
      database.jenkinsJobs().addListener( new FunctionListChangeListenerImpl<>( 
               newBuilds::add, null 
      ) );
   }//End Constructor

   /**
    * Method to get the currently identified new builds and clear that cache.
    * @return the {@link JenkinsJob}s identified as new.
    */
   public List< JenkinsJob > getAndClearNewBuilds() {
      List< JenkinsJob > copy = new ArrayList<>( newBuilds );
      newBuilds.clear();
      return copy;
   }//End Method

}//End Class
