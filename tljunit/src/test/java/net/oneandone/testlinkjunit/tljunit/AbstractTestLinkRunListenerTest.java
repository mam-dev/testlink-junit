/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * @author mirko
 *
 */
abstract public class AbstractTestLinkRunListenerTest {

    final void hasValue(final Xpp3Dom testCase, final String name) {
        final Xpp3Dom[] children = testCase.getChildren(name);
        assertEquals(1, children.length);
        assertFalse(children[0].getValue().isEmpty());
    }

    /**
     * @param results
     * @return
     */
    final int checkResultsAndCountTestCasesWithExternalId(final Xpp3Dom results) {
        int testCasesWithExternalId = 0;
        for (final Xpp3Dom testCase : results.getChildren()) {
            if (testCase.getAttribute("external_id") != null) {
                testCasesWithExternalId++;
            }
            hasValue(testCase, "tester");
            hasValue(testCase, "timestamp");
            hasValue(testCase, "result");
            hasValue(testCase, "notes");
        }
        return testCasesWithExternalId;
    }

    final int getIgnoredTestCases(final Xpp3Dom results) {
        int ignoreds = 0;
        for (final Xpp3Dom testCase : results.getChildren()) {
            final String result = testCase.getChild("result").getValue();
            ignoreds += result.equals("b") ? 1 : 0;
        }
        return ignoreds;
    }
}
