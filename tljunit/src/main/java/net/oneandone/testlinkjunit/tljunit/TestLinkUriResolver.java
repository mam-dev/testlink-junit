/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import java.net.URI;

import net.oneandone.testlinkjunit.tljunit.TestLinkId.ExternalTestLinkId;
import net.oneandone.testlinkjunit.tljunit.TestLinkId.InternalTestLinkId;

/**
 * Resolves links to the
 */
class TestLinkUriResolver {

    private final URI baseUri;

    /**
     * The constructor normalizes the URI by adding a trailing slash when it is missing.
     * 
     * @param baseUri
     *            without <tt>index.php</tt>, eg <tt>http://testlink.sourceforge.net/demo/</tt>.
     */
    TestLinkUriResolver(URI baseUri) {
        final String asciiUri = baseUri.toASCIIString();
        if (asciiUri.endsWith("/")) {
            this.baseUri = baseUri;
        } else {
            this.baseUri = URI.create(asciiUri + "/");
        }
    }

    /**
     * http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750
     * 
     * @param internalTestLinkId
     *            id of the testcase
     * @return an URI pointing to the printview of the last version of the testcase description.
     */
    URI fromTestLinkId(final InternalTestLinkId internalTestLinkId) {
        return baseUri.resolve(String.format("lib/testcases/tcPrint.php?testcase_id=%s", internalTestLinkId.getId()));
    }

    /**
     * http://testlink.sourceforge.net/demo/lib/testcases/archiveData.php?targetTestCase=SM-1&edit=testcase&allowedit=0
     * 
     * @param externalTestLinkId
     *            id of the testcase
     * @return an URI pointing to the printview of the last version of the testcase description.
     */
    URI fromTestLinkId(final ExternalTestLinkId externalTestLinkId) {
        return baseUri.resolve(String.format("lib/testcases/archiveData.php?targetTestCase=%s&edit=testcase&allowedit=0", externalTestLinkId.getId()));
    }
}
