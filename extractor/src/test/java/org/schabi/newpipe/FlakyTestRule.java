package org.schabi.newpipe;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.schabi.newpipe.downloader.DownloaderType;

import javax.annotation.Nonnull;

public class FlakyTestRule implements TestRule {

    @Override
    @Nonnull
    public Statement apply(@Nonnull Statement base, @Nonnull Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String downloader = System.getProperty("downloader");
                Assume.assumeTrue(
                        "The test is not reliable against a website and thus skipped",
                        downloader == null
                                || !downloader.equalsIgnoreCase(DownloaderType.REAL.toString())
                );

                base.evaluate();
            }
        };
    }
}
