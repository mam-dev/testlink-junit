/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private final URI expectedUri;
    private final TestLinkId<?> testLinkId;

    @Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(
            new Object[]{
                URI.create("http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750"),
                new TestLinkId.InternalTestLinkId(2750L)
            },
            new Object[]{
                URI.create("http://testlink.sourceforge.net/demo/lib/testcases/archiveData.php?targetTestCase=SM-1&edit=testcase&allowedit=0"),
                new TestLinkId.ExternalTestLinkId("SM-1")
            }
        );
    }

    public TestLinkUriResolverTest(URI expectedUri, final TestLinkId<?> testLinkId) {
        this.testLinkId = testLinkId;
        this.expectedUri = expectedUri;
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
        assertEquals(expectedUri, actual);
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
        assertEquals(expectedUri, actual);
    }
}
