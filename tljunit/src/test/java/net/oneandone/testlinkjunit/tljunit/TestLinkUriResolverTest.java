/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author mirko
 */
@RunWith(Parameterized.class)
public class TestLinkUriResolverTest {

    private final URI expected;
    private final TestLinkId<?> testLinkId;

    @Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(
                new Object[]{"http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750", new TestLinkId.InternalTestLinkId(2750L)},
                new Object[]{"http://testlink.sourceforge.net/demo/lib/testcases/archiveData.php?targetTestCase=SM-1&edit=testcase&allowedit=0", new TestLinkId.ExternalTestLinkId("SM-1")});
    }

    public TestLinkUriResolverTest(String uri, final TestLinkId<?> testLinkId) {
        this.testLinkId = testLinkId;
        expected = URI.create(uri);
    }

    /**
     * Test method for
     * {@link net.oneandone.testlinkjunit.tljunit.TestLinkUriResolver#fromTestLinkId(net.oneandone.testlinkjunit.tljunit.TestLinkId.InternalTestLinkId)}
     * .
     */
    @Test
    public void testFromInternalTestLinkId() {
        final TestLinkUriResolver resolver = new TestLinkUriResolver(
                URI.create("http://testlink.sourceforge.net/demo/"));
        final URI actual = resolver.fromTestLinkId(testLinkId);
        assertEquals(expected, actual);
    }

    /**
     * Test method for
     * {@link net.oneandone.testlinkjunit.tljunit.TestLinkUriResolver#fromTestLinkId(net.oneandone.testlinkjunit.tljunit.TestLinkId.InternalTestLinkId)}
     * .
     */
    @Test
    public void testFromInternalTestLinkIdWithoutTrailingSlash() {
        final TestLinkUriResolver resolver = new TestLinkUriResolver(URI.create("http://testlink.sourceforge.net/demo"));
        final URI actual = resolver.fromTestLinkId(testLinkId);
        assertEquals(expected, actual);
    }
}
