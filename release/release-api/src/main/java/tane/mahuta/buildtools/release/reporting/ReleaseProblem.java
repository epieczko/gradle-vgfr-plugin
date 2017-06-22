package tane.mahuta.buildtools.release.reporting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * An reporting encountered while releasing.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@EqualsAndHashCode
@ToString
public class ReleaseProblem {

    @Getter(onMethod = @__(@Nonnull))
    private final Severity severity;
    @Getter(onMethod = @__(@Nonnull))
    private final String messageFormat;
    @Getter(onMethod = @__(@Nonnull))
    private final Object[] formatArguments;

    public ReleaseProblem(final Severity severity, final String messageFormat, final Object[] formatArguments) {
        this.severity = severity;
        this.messageFormat = messageFormat;
        this.formatArguments = formatArguments;
    }

    public static ReleaseProblem.ReleaseProblemBuilder builder() {
        return new ReleaseProblem.ReleaseProblemBuilder();
    }

    public static class ReleaseProblemBuilder {

        private static final Object[] EMPTY_ARGS = new Object[0];

        private Severity severity;
        private String messageFormat;
        private Object[] formatArgs;

        public ReleaseProblemBuilder severity(@Nonnull final Severity severity) {
            this.severity = severity;
            return this;
        }

        public ReleaseProblemBuilder formatArgs(@Nullable final Object... args) {
            this.formatArgs = args;
            return this;
        }

        public ReleaseProblemBuilder messageFormat(@Nonnull final String message) {
            this.messageFormat = message;
            return this;
        }

        public ReleaseProblem build() {
            Objects.requireNonNull(severity, "Severity was not set.");
            Objects.requireNonNull(messageFormat, "Message format was not set.");
            return new ReleaseProblem(severity, messageFormat, Optional.ofNullable(formatArgs).orElse(EMPTY_ARGS));
        }

    }
}
