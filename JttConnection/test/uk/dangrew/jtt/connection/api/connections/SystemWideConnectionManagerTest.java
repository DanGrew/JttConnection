/*
 * ----------------------------------------
 *     Jenkins Test Tracker Connection
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2017
 * ----------------------------------------
 */
package uk.dangrew.jtt.connection.api.connections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SystemWideConnectionManagerTest {

   private SystemWideConnectionManager systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new SystemWideConnectionManager();
   }//End Method

   @Test public void shouldProvideSharedResource() {
      assertThat( systemUnderTest.get(), is( new SystemWideConnectionManager().get() ) );
   }//End Method

}//End Class
