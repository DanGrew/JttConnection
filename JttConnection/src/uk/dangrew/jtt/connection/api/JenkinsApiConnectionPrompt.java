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
import java.util.function.BiFunction;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.connection.login.JenkinsLoginPrompt;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JenkinsApiConnectionPrompt} is responsible for connecting to the {@link JenkinsApiImpl}. This is 
 * a simple extraction to allow login for other systems.
 */
public class JenkinsApiConnectionPrompt {

   private final ExternalApi api;
   private final BiFunction< ExternalApi, DigestViewer, JenkinsLoginPrompt > promptSupplier;
   
   /**
    * Constructs a new {@link JenkinsApiConnectionPrompt}.
    */
   public JenkinsApiConnectionPrompt() {
      this( 
               JenkinsLoginPrompt::new,
               new JenkinsApiImpl()
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsApiConnectionPrompt}.
    * @param api the {@link ExternalApi} to connect to.
    */
   public JenkinsApiConnectionPrompt( ExternalApi api ) {
      this( 
               JenkinsLoginPrompt::new,
               api
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsApiConnectionPrompt}.
    * @param promptSupplier the supplier of the {@link JenkinsLoginPrompt}.
    * @param api the {@link ExternalApi} to connect to.
    */
   JenkinsApiConnectionPrompt( 
            BiFunction< ExternalApi, DigestViewer, JenkinsLoginPrompt > promptSupplier,
            ExternalApi api
   ){
      this.promptSupplier = promptSupplier;
      this.api = api;
   }//End Constructor
   
   /**
    * Method to connect to the {@link JenkinsApiImpl}.
    * @param digest the {@link DigestViewer} for presenting login information.
    * @return the {@link ExternalApi} is successful, null otherwise.
    */
   public ExternalApi connect( DigestViewer digest ) {
      JenkinsLoginPrompt prompt = promptSupplier.apply( api, digest );
      Optional< Boolean > result = prompt.friendly_showAndWait();
      if ( result.get() ) {
         return api;
      } else {
         return null;
      }
   }//End Method

}//End Class
