/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * @author mirko
 *
 */
abstract public class AbstractTestLinkRunListenerTest {

    final static String[] REQUIRED_ELEMENTS = new String[] { "tester", "timestamp", "result", "notes" };

    final void assertHasExactlyOneNamedNonEmptyElement(final Xpp3Dom testCase, final String name) {
        final Xpp3Dom[] children = testCase.getChildren(name);
        assertEquals("Expected exactly one " + name, 1, children.length);
        assertFalse("Expected " + name + " not to be empty.", children[0].getValue().isEmpty());
    }

    final int countTestsWithExternalIdfinal(Xpp3Dom results) {
        final AtomicInteger externalIds = new AtomicInteger();
        new ForAllTestCases(results) {
            @Override
            void apply(Xpp3Dom each) {
                if (each.getAttribute("external_id") != null) {
                    externalIds.incrementAndGet();
                }
            }
        }.run();
        return externalIds.intValue();
    }

    final void assertAllTestCasesHaveRequiredElements(final Xpp3Dom results) {
        new ForAllTestCases(results) {
            @Override
            void apply(Xpp3Dom each) {
                for (final String requiredElement : REQUIRED_ELEMENTS) {
                    assertHasExactlyOneNamedNonEmptyElement(each, requiredElement);
                }
            }
        }.run();
    }

    final int countIgnoredTests(final Xpp3Dom results) {
        final AtomicInteger ignored = new AtomicInteger();
        new ForAllTestCases(results) {
            @Override
            void apply(Xpp3Dom each) {
                final String result = each.getChild("result").getValue();
                ignored.addAndGet(result.equals("b") ? 1 : 0);
            }
        }.run();
        return ignored.intValue();
    }

    abstract static class ForAllTestCases implements Runnable {

        private final Xpp3Dom results;

        public ForAllTestCases(final Xpp3Dom results) {
            this.results = results;
        }
        /** {@inheritDoc} */
        @Override
        public void run() {
            for (final Xpp3Dom testCase : results.getChildren()) {
                apply(testCase);
            }
        }

        abstract void apply(Xpp3Dom each);
    }
}
