package tane.mahuta.gradle.plugin;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * @author christian.heike@icloud.com
 *         Created on 24.06.17.
 */
public class OnlineRule implements TestRule {

    private Boolean online;

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                assumeOnline();
            }
        };
    }

    public void assumeOnline() {
        Assume.assumeTrue("Could not connect to the internet", isOnline());
    }

    public boolean isOnline() {
        return this.online = Optional.ofNullable(online).orElseGet(this::checkOnline);
    }

    protected boolean checkOnline() {
        try (InputStream is = new URI("http://www.google.de").toURL().openStream()) {
            return true;
        } catch (final Exception ex) {
            return false;
        }
    }
}
