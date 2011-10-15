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
 * Class which writes an additional TestLink XML file as described in Testlink's <a
 * href="http://www.teamst.org/_tldoc/1.8/user_manual.pdf">user manual</a>.
 *
 * @author mirko
 */
public class TestLinkRunListener extends RunListener {

    private final PrintStream out;

    private final InTestLinkStrategy inTestLinkstrategy;

    private final NoTestLinkStrategy noTestLinkStrategy;

    /**
     * Write results to system property "testlink.results" or to "target/testlink.xml" by default.
     *
     * @throws FileNotFoundException when the file could not be written.
     */
    public TestLinkRunListener() throws FileNotFoundException {
        this(new PrintStream(
                new BufferedOutputStream(
                        new FileOutputStream(System.getProperty("testlink.results", "target/testlink.xml")))),
             System.getProperty("testlink.userName", System.getProperty("user.name", "UNKNOWN")));
    }

    /**
     * @param out to be used for writing the testlink xml file.
     * @param inTestLinkStrategy
     * @param noTestLinkStrategy
     */
    public TestLinkRunListener(final PrintStream out, final String userName) {
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
        selectStrategy(failure.getDescription()).addFailure(failure);
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
