/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api;

import java.util.Optional;
import java.util.function.Function;

import uk.dangrew.jtt.connection.login.JenkinsLoginPrompt;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JenkinsApiConnectionPrompt} is responsible for connecting to the {@link JenkinsApiImpl}. This is 
 * a simple extraction to allow login for other systems.
 */
public class JenkinsApiConnectionPrompt {

   private final Function< DigestViewer, JenkinsLoginPrompt > promptSupplier;
   
   /**
    * Constructs a new {@link JenkinsApiConnectionPrompt}.
    */
   public JenkinsApiConnectionPrompt() {
      this( 
               JenkinsLoginPrompt::new
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsApiConnectionPrompt}.
    * @param promptSupplier the supplier of the {@link JenkinsLoginPrompt}.
    */
   JenkinsApiConnectionPrompt( 
            Function< DigestViewer, JenkinsLoginPrompt > promptSupplier
   ){
      this.promptSupplier = promptSupplier;
   }//End Constructor
   
   /**
    * Method to connect to the {@link JenkinsApiImpl}.
    * @param digest the {@link DigestViewer} for presenting login information.
    * @return true if successful.
    */
   public boolean connect( DigestViewer digest ) {
      JenkinsLoginPrompt prompt = promptSupplier.apply( digest );
      Optional< Boolean > result = prompt.friendly_showAndWait();
      return result.get();
   }//End Method

}//End Class
