package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertTrue;

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
