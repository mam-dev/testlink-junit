/**
 * <p>
 * By annotating your tests with {@link net.oneandone.testlinkjunit.tljunit.TestLink} 
 * test results will be written to a <tt>XML</tt> file which may be imported to a running  
 * <a href="http://www.teamst.org/">Testlink</a> instance as described in the 
 * <a href="http://www.teamst.org/_tldoc/1.8/user_manual.pdf">user manual</a>. 
 * </p> 
 * <h1>Usage</h1> 
 * <h2>Annotating your tests</h2> 
 * <p> 
 * You have to annotate your tests and provide either an <tt>externalId</tt> or <tt>internalId</tt> 
 * </p>
 * <pre>
 * &#064;Test
 * &#064;TestLink(internalId=1)
 * public void testPassed() {
 *    assertTrue(true); 
 * }
 * </pre>
 * <pre>
 * &#064;Test
 * &#064;TestLink(internalId=2)
 * &#064;Ignore("Just ignore this")
 * public void testIgnored() {
 *    assertTrue(true);
 * }
 * </pre>
 * <pre>
 * &#064;Test
 * &#064;TestLink(externalId="ASSUMPTION_FAILED")
 * public void testWithFailingAssumption() {
 *    assumeTrue(false);
 * }
 * </pre>
 * <pre>
 * &#64;Test
 * &#64;TestLink(externalId="PROJECT-1")
 * public void testExternalId() {
 *     assertTrue(true);
 * }
 * </pre>
 * <p>
 * Tests annotated with {@link org.junit.Ignore} will be marked as <tt>BLOCKED</tt> as well as tests with failing assumptions.
 * </p>
 * <h2>Running tests with the maven-surefire-plugin</h2>
 * <p>
 * You have to configure the surefire plugin to use the additional
 * {@link net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener}. 
 * As can be seen below, the username and file-location for the result file may be provided as system properties.
 * </p>
 * <pre>{@code
 * <build>
 *    <plugins>
 *        <plugin>
 *            <groupId>org.apache.maven.plugins</groupId>
 *            <artifactId>maven-surefire-plugin</artifactId>
 *            <version>2.10</version>
 *            <dependencies>
 *                <dependency>
 *                    <groupId>net.oneandone.testlinkjunit</groupId>
 *                    <artifactId>tljunit</artifactId>
 *                    <version>1.X</version>
 *                </dependency>
 *            </dependencies>
 *            <configuration>
 *                <properties>
 *                    <property>
 *                        <name>listener</name>
 *                        <value>net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener</value>
 *                    </property>
 *                </properties>
 *                <systemPropertyVariables>
 *                    <testlink.results>target/my-testlink.xml</testlink.results>
 *                    <testlink.userName>memyselfandi</testlink.userName>
 *                </systemPropertyVariables>
 *            </configuration>
 *        </plugin>
 *    </plugins>
 * </build>
 * }</pre>
 * <p>
 * Now running <kbd>mvn test</kbd> will run your tests and put the resulting TestLink XML file into
 * <tt>target/my-testlink.xml</tt> using <tt>memyselfandi</tt> as name of the user who executed the test run.
 * </p>
 * <h2>Running tests in Eclipse</h2>
 * <p>
 * To run a test from Eclipse, add a main method which will collect the tests:
 * </p>
 * <pre>
 * package net.oneandone.testlinkjunit.eclipse;
 * 
 * import static org.junit.Assert.assertTrue;
 * 
 * import java.io.FileNotFoundException;
 * 
 * import net.oneandone.testlinkjunit.tljunit.TestLink;
 * import net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener;
 * 
 * import org.junit.Test;
 * import org.junit.runner.JUnitCore;
 * 
 * public class EclipseTest {
 * 
 *    &#64;Test
 *    &#64;TestLink(externalId="ECLIPSE_TEST")
 *    public void test() {
 *        assertTrue(true);
 *    }
 * 
 *    public static void main(String[] args) throws FileNotFoundException {
 *        final JUnitCore core = new JUnitCore();
 *        core.addListener(new TestLinkXmlRunListener());
 *        core.run(EclipseTest.class);
 *    }
 * }
 * </pre>
 */
package net.oneandone.testlinkjunit.tljunit;

