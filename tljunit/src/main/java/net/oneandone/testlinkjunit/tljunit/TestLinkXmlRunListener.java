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
 * For usage with the <a href="http://maven.apache.org/plugins/maven-surefire-plugin/"><tt>maven-surefire-plugin</tt></a>
 * as well as Eclipse take a look into the <a href="package-summary.html">package summary</a>. 
 */
public class TestLinkXmlRunListener extends RunListener {

    private final PrintStream out;

    private final InTestLinkStrategy inTestLinkstrategy;

    private final NoTestLinkStrategy noTestLinkStrategy;

    /**
     * Parameters are taken via System properties:
     * <dl>
     * <dt><code>testlink.results</code></dt>
     * <dd>Writes results to this filename (<tt>target/testlink.xml</tt> by default).</dd>
     * <dt><code>testlink.userName</code></dt>
     * <dd>To be used as tester. (falls back to system property <tt>user.name</tt> by default).</dd>
     * </dl>
     * Writes results to a file given by system property <tt>testlink.results<tt> (<tt>target/testlink.xml</tt> by default).
     * 
     * @throws FileNotFoundException
     *             when the file could not be written.
     */
    public TestLinkXmlRunListener() throws FileNotFoundException {
        this(new PrintStream(
                new BufferedOutputStream(
                        new FileOutputStream(System.getProperty("testlink.results", "target/testlink.xml")))),
             System.getProperty("testlink.userName", System.getProperty("user.name")));
    }

    /**
     * Writes results to <tt>out</tt> using <tt>userName</tt> as tester.
     *
     * @param out
     *            to be used for writing the testlink xml file.
     * @param userName
     *            to be used as tester.
     */
    public TestLinkXmlRunListener(final PrintStream out, final String userName) {
        this.out = out;
        this.inTestLinkstrategy = new InTestLinkStrategy(userName);
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
        selectStrategy(description).addIgnore(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        selectStrategy(failure.getDescription()).addFailureOrAssumptionFailure(failure, TestState.f);
    }

    /** {@inheritDoc} */
    @Override
    public void testAssumptionFailure(final Failure failure) {
        super.testAssumptionFailure(failure);
        selectStrategy(failure.getDescription()).addFailureOrAssumptionFailure(failure, TestState.b);
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        selectStrategy(description).addFinished(description);
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
