package tane.mahuta.gradle.plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author christian.heike@icloud.com
 *         Created on 07.06.17.
 */
public class GradlePropertiesFile implements Map<Object, Object> {

    private final Properties properties = new Properties();
    private final ProjectBuilderTestRule testRule;

    GradlePropertiesFile(@Nonnull final ProjectBuilderTestRule testRule) {
        this.testRule = testRule;
    }

    private void storeProperties() {
        try (FileWriter w = new FileWriter(new File(testRule.getRoot(), "gradle.storage"))) {
            properties.store(w, "Test gradle.storage");
        } catch (IOException e) {
            throw new IllegalStateException("Could not write property file");
        }
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return properties.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        final Object result = properties.put(key, value);
        storeProperties();
        return result;
    }

    @Override
    public Object remove(Object key) {
        final Object result = properties.remove(key);
        storeProperties();
        return result;
    }

    @Override
    public void putAll(Map<?, ?> m) {
        properties.putAll(m);
        storeProperties();
    }

    @Override
    public void clear() {
        properties.clear();
        storeProperties();
    }

    @Override
    public Set<Object> keySet() {
        return properties.keySet();
    }

    @Override
    public Collection<Object> values() {
        return properties.values();
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }
}
