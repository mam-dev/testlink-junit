package net.oneandone.testlinkjunit.tljunit;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

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
 *           <testlink.tester>memyselfandi</testlink.userName>
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
public class TestLinkXmlRunListener extends RunListener {

    private final PrintStream out;

    private final InTestLinkStrategy inTestLinkstrategy;

    private final NoTestLinkStrategy noTestLinkStrategy;

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
        this.out = out;
        this.inTestLinkstrategy = new InTestLinkStrategy(tester);
        this.noTestLinkStrategy = new NoTestLinkStrategy();
    }

    /** {@inheritDoc} */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        selectStrategy(description).addNewTestCase(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        final TestLinkStrategy strategy = selectStrategy(description);
        strategy.addNewTestCase(description);
        strategy.setBlockedWhenIgnored(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        selectStrategy(failure.getDescription()).setFailed(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testAssumptionFailure(final Failure failure) {
        super.testAssumptionFailure(failure);
        selectStrategy(failure.getDescription()).setBlockedWhenAssumptionFailed(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        selectStrategy(description).setPassedWhenNoFailure(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        out.print(String.valueOf(inTestLinkstrategy.getResults()));
        out.close();
    }

    /**
     * Selects strategy for test cases with or without {@link TestLink} annotation.
     * 
     * @param description
     *            of the test case.
     * @return the currently valid strategy
     */
    TestLinkStrategy selectStrategy(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
            return inTestLinkstrategy;
        } else {
            return noTestLinkStrategy;
        }
    }

    /**
     * Returns the results of the {@link InTestLinkStrategy} for unit testing.
     * 
     * @return the results
     */
    Xpp3Dom getResults() {
        return inTestLinkstrategy.getResults();
    }
}
