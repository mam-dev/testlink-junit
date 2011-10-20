package net.oneandone.testlinkjunit.tljunit;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.runner.Result;

/**
 * Writes an additional TestLink XML file as described in Testlink's <a
 * href="http://www.teamst.org/_tldoc/1.8/user_manual.pdf">user manual</a>.
 * 
 * <h3>Usage with maven-surefire-plugin</h3>
 * 
 * <pre>
 * {@code
 * <properties>
 *   <tljunit.version>X.X<tljunit.version>
 * </properties>
 * [...]
 *   <plugin>
 *     <groupId>org.apache.maven.plugins</groupId>
 *     <artifactId>maven-surefire-plugin</artifactId>
 *     <version>2.10</version>
 *     <dependencies>
 *       <dependency>
 *         <groupId>net.oneandone.testlinkjunit</groupId>
 *         <artifactId>tljunit</artifactId>
 *         <version>}${tljunit.version}{@code</version>
 *       </dependency>
 *     </dependencies>
 *     <configuration>
 *       <properties>
 *         <property>
 *           <name>listener</name>
 *           <value>net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener</value>
 *         </property>
 *       </properties>
 *         <systemPropertyVariables>
 *           <testlink.results>target/my-testlink.xml</testlink.results>
 *           <testlink.tester>memyselfandi</testlink.tester>
 *         </systemPropertyVariables>
 *     </configuration>
 *   </plugin>
 * [...]
 *   <dependencies>
 *       <dependency>
 *         <groupId>net.oneandone.testlinkjunit</groupId>
 *         <artifactId>tljunit</artifactId>
 *         <version>}${tljunit.version}{@code</version>
 *       </dependency>
 *   </dependencies>
 * [...]
 * }
 * </pre>
 * 
 * <h3>Using from <tt>main</tt></h3>
 * 
 * <pre>
 * public static void main(String[] args) throws FileNotFoundException {
 *     final JUnitCore core = new JUnitCore();
 *     core.addListener(new TestLinkXmlRunListener(System.out, &quot;name_of_tester&quot;));
 *     core.run(MyTest.class);
 * }
 * </pre>
 */
public class TestLinkXmlRunListener extends AbstractTestLinkRunListener<InTestLinkXmlStrategy> {

    final PrintStream out;

    /**
     * Instantiates {@link TestLinkXmlRunListener#TestLinkXmlRunListener(PrintStream, String)} with parameters taken
     * from System properties.
     * 
     * <dl>
     * <dt><code>testlink.results</code></dt>
     * <dd>Results are written to this filename (<tt>target/testlink.xml</tt> by default).</dd>
     * <dt><code>testlink.tester</code></dt>
     * <dd>To be used as name of the tester. (falls back to system property <tt>user.name</tt> by default).</dd>
     * </dl>
     * 
     * @throws FileNotFoundException
     *             when the file could not be written.
     */
    public TestLinkXmlRunListener() throws FileNotFoundException {
        this(new PrintStream(new BufferedOutputStream(new FileOutputStream(System.getProperty("testlink.results",
                "target/testlink.xml")))), System.getProperty("testlink.tester", System.getProperty("user.name")));
    }

    /**
     * Writes results to <tt>out</tt> using <tt>tester</tt> as name of the tester.
     * 
     * @param out
     *            the xml data is written to.
     * @param tester
     *            name of the tester.
     */
    public TestLinkXmlRunListener(final PrintStream out, final String tester) {
        super(new InTestLinkXmlStrategy(tester));
        this.out = out;
    }

    /** {@inheritDoc} */
    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        out.print(String.valueOf(getInTestLinkStrategy().getResults()));
        out.close();
    }

    /**
     * Returns the results of the {@link InTestLinkXmlStrategy} for unit testing.
     * 
     * @return the results
     */
    Xpp3Dom getResults() {
        return getInTestLinkStrategy().getResults();
    }
}
