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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.maven.shared.utils.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * {@link org.junit.runner.notification.RunListener} which logs everything to Testlink compatible XML file.
 *
 * @author Mirko Friedenhagen
 */
class InTestLinkXmlRunListener extends AbstractInTestLinkRunListener {

    /** Dom-creator for the total results. */
    private final Xpp3Dom results;

    /** Name of the tester, default to the System property user.name. */
    private final String testerName;

    /** Dom-creator for the results of the current testcase. */
    private final ThreadLocal<Xpp3Dom> currentTestCase = new ThreadLocal<Xpp3Dom>();

    /**
     * @param testerName Name of the tester, default to the System property user.name.
     */
    public InTestLinkXmlRunListener(final String testerName) {
        results = new Xpp3Dom("results");
        this.testerName = testerName;
    }

    /**
     * {@inheritDoc}
     * 
     * {@link TestLink} annotation must have either {@link TestLink#internalId()} or {@link TestLink#externalId()} set.
     */
    @Override
    public void testStarted(Description description) {
        resetCurrentFailure();
        final Xpp3Dom testCase = new Xpp3Dom("testcase");
        setCurrentTestCase(testCase);
        testCase.addChild(createTester(testerName));
        testCase.addChild(createTimeStamp(new Date()));
        // This is the only place where we access results, so we should be fine to just synchronize here.
        synchronized (results) {
            results.addChild(testCase);
        }
        final TestLinkId<?> id = TestLinkId.fromDescription(description);
        testCase.setAttribute(id.getType(), String.valueOf(id.getId()));
    }

    /** {@inheritDoc} */
    @Override
    public void testIgnored(Description description) {
        testStarted(description);
        final Xpp3Dom testCase = getCurrentTestCase();
        testCase.addChild(createResult(TestState.blocked));
        final String message = description.getAnnotation(Ignore.class).value();
        testCase.addChild(createNotes(String.format("'%s' BLOCKED because '%s'.", description.getDisplayName(), message)));
    }

    /** {@inheritDoc} */
    @Override
    public void testAssumptionFailure(Failure failure) {
        setFailedOrIgnoredForFailureOrAssumptionFailure(failure, TestState.blocked);

    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) {
        setFailedOrIgnoredForFailureOrAssumptionFailure(failure, TestState.failed);
    }

    /**
     * Attaches the notes of the Failure to the result set depending on the TestState.
     *
     * Ignored Testcases (or those where an Assumption failed) are marked as BLOCKED,
     * otherwise report as FAILED.
     *
     * @param failure   either a real Failure or a blocked testcase.
     * @param testState FAILED or BLOCKED.
     */
    private void setFailedOrIgnoredForFailureOrAssumptionFailure(Failure failure, TestState testState) {
        setCurrentFailure(failure);
        final Xpp3Dom testCase = getCurrentTestCase();
        testCase.addChild(createResult(testState));
        final String message = failure.getMessage();
        final Xpp3Dom notes;
        if (message != null) {
            notes = createNotes(String.format("'%s' " + testState.getDescription() + " because '%s'.",
                    failure.getTestHeader(), message));
        } else {
            notes = createNotes(String.format("'%s' " + testState.getDescription() + " because '%s'.",
                    failure.getTestHeader(), failure.getTrace()));
        }
        testCase.addChild(notes);
    }

    /**
     * {@inheritDoc}
     * 
     * This will set the test to PASSED only when we have no {@link InTestLinkXmlRunListener#currentFailure}.
     */
    @Override
    public void testFinished(Description description) {
        if (hasPassed()) {
            final Xpp3Dom testCase = getCurrentTestCase();
            testCase.addChild(createResult(TestState.passed));
            testCase.addChild(createNotes(String.format("'%s' PASSED.", description.getDisplayName())));
        }
    }

    /**
     * Creates a new element.
     *
     * @param elementName elementName of XML
     * @param text value of XML
     * @return dom
     */
    private Xpp3Dom createElementWithText(String elementName, String text) {
        final Xpp3Dom element = new Xpp3Dom(elementName);
        element.setValue(text);
        return element;
    }
    /**
     * Creates a new tester element filled with the tester.
     * 
     * @param userName
     *            name of the user got from system property {@code testlink.userName} or {@code user.name}.
     * @return &lt;tester&gt; element.
     */
    Xpp3Dom createTester(final String userName) {
        return createElementWithText("tester", userName);
    }

    /**
     * Creates a new timestamp element.
     * 
     * @param date
     *            of the test run.
     * @return &lt;timestamp&gt; element.
     */
    Xpp3Dom createTimeStamp(final Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return createElementWithText("timestamp", dateFormat.format(date));
    }

    /**
     * Creates a new notes element.
     * 
     * @param notesValue
     *            additional notes.
     * @return &lt;notes&gt; element.
     */
    Xpp3Dom createNotes(final String notesValue) {
        return createElementWithText("notes", notesValue);
    }

    /**
     * Creates a new result element.
     * 
     * @param testState
     *            to report
     * @return &lt;result&gt; element.
     */
    Xpp3Dom createResult(final TestState testState) {
        return createElementWithText("result", testState.getState());
    }

    /**
     * @return the results
     */
    Xpp3Dom getResults() {
        return results;
    }

    /**
     * @return the currentTestCase
     */
    private Xpp3Dom getCurrentTestCase() {
        return currentTestCase.get();
    }

    /**
     * @param currentTestCase
     *            the currentTestCase to set
     */
    private void setCurrentTestCase(Xpp3Dom currentTestCase) {
        this.currentTestCase.set(currentTestCase);
    }
}
