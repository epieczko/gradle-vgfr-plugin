package tane.mahuta.buildtools.version.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import tane.mahuta.buildtools.version.VersionStorage;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link VersionStorage} for property files.
 *
 * @author christian.heike@icloud.com
 *         Created on 28.05.17.
 */
@Slf4j
public class PropertyVersionStorage implements VersionStorage {

    private final File file;
    private final Pattern pattern;
    private final String propertyName;

    public PropertyVersionStorage(@Nonnull final File file, @Nonnull final String propertyName) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("File " + String.valueOf(file) + " is not a file.");
        }

        this.file = file;
        this.propertyName = propertyName;
        this.pattern = Pattern.compile("^(\\s*" + Pattern.quote(propertyName) + "\\s*[=\\s:]\\s*).+$");
    }

    @Override
    @SneakyThrows
    public void store(final String version) {
        final File temporaryFile = createTemporaryCopy(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(temporaryFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String line;
            boolean replaced = false;
            while ((line = reader.readLine()) != null) {
                final Matcher m = pattern.matcher(line);
                if (m.matches()) {
                    skipMultiLines(reader, line);
                    line = m.replaceAll("$1" + version);
                    replaced = true;
                }
                writer.write(line);
                writer.newLine();
            }
            if (!replaced) {
                writer.write(propertyName);
                writer.write("=");
                writer.write(version);
                writer.newLine();
            }
        }
    }

    private static void skipMultiLines(@Nonnull final  BufferedReader reader, @Nonnull String line) throws IOException {
        while (line != null && line.endsWith("\\")) {
            line = reader.readLine();
        }
    }

    @SneakyThrows
    private final File createTemporaryCopy(@Nonnull final File file) {
        final File result = File.createTempFile("temporary", "storage");
        FileUtils.copyFile(file, result);
        return result;
    }


}
