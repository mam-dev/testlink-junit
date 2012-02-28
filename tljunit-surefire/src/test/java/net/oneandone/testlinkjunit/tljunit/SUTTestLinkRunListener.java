/*
 * Copyright 2012 1&1.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeThat;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SUTTestLinkRunListener {

    @Test
    @TestLink(externalId = "T1")
    public void testSuccessExternal() {
        assertTrue(true);
    }

    @Test
    @TestLink(internalId = 4)
    public void testSuccessInternal() {
        assertTrue(true);
    }

    @Test
    @TestLink(externalId = "T2")
    public void testFailed() {
        assertTrue("Failed", false);
    }

    @Test
    @TestLink(externalId = "T2_WITHOUT_MESSAGE")
    public void testFailedWithOutMessage() {
        assertTrue(false);
    }

    @Test
    @TestLink(internalId = 5)
    public void testSuccessInternal2() {
        assertTrue(true);
    }

    @Test
    @TestLink(externalId = "T3")
    @Ignore("Does not run.")
    public void testIgnore() {
        assertTrue(true);
    }

    @Test
    @TestLink(externalId = "ASSUMPTION_FAILED")
    public void testFailingAssumption() {
        assumeThat(0, is(1));
    }

    @Test
    public void testFailingAssumptionWithoutTestLinkAnnotation() {
        assumeNoException(new IllegalStateException("Could not connect to server"));
    }

    @Test
    public void testNoTestLinkAnnotationSuccess() {
        assertTrue(true);
    }

    @Test
    public void testNoTestLinkAnnotationFailure() {
        assertTrue(false);
    }

    @Test
    @Ignore
    public void testNoTestLinkAnnotationIgnore() {
        assertTrue(false);
    }
}
