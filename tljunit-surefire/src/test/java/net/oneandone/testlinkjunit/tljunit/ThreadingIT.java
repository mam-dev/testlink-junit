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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author mirko
 * 
 */
public class ThreadingIT extends AbstractTestLinkRunListenerTest {

    private final ExecutorService pool = Executors.newFixedThreadPool(50);

    private final TestLinkXmlRunListener listener;

    private final PrintStream out;

    public ThreadingIT() throws FileNotFoundException {
        out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // just do nothing
            }
        });
        listener = new TestLinkXmlRunListener(out, "donald");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        pool.shutdown();
        pool.awaitTermination(100, TimeUnit.SECONDS);
        final Xpp3Dom results = listener.getResults();
        assertAllTestCasesHaveRequiredElements(results);
        assertEquals(700, results.getChildCount());
        assertEquals(500, countTestsWithExternalIdfinal(results));
        assertEquals(200, countIgnoredTests(results));
        final PrintStream stream = new PrintStream(new File("target/parallel-testlink.xml"));
        try {
            stream.print(results.toString());
        } finally {
            stream.close();
        }
    }

    @TestLink(externalId = "testParallel")
    @Test
    public void testParallel() throws FileNotFoundException {
        for (int i = 0; i < 100; i++) {
            final JUnitCore core = new JUnitCore();
            core.addListener(listener);
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    final Result result = core.run(SUTTestLinkRunListener.class);
                    assertEquals(9, result.getRunCount());
                    assertEquals(2, result.getIgnoreCount());
                }
            });
        }
    }
}
