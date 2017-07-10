package tane.mahuta.gradle.plugin;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * @author christian.heike@icloud.com
 *         Created on 24.06.17.
 */
public class OnlineCheck {

    private static class InstanceHolder {
        private static final OnlineCheck INSTANCE = new OnlineCheck();
    }

    private Boolean online;

    public static boolean isOnline() {
        return InstanceHolder.INSTANCE.isOnlineImpl();
    }

    private boolean isOnlineImpl() {
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
