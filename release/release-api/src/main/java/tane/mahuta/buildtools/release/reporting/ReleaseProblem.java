package tane.mahuta.buildtools.release.reporting;

import lombok.*;

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
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleaseProblem {

    @Getter(onMethod = @__(@Nonnull))
    private final Severity severity;
    @Getter(onMethod = @__(@Nonnull))
    private final String messageFormat;
    @Getter(onMethod = @__(@Nonnull))
    private final Object[] formatArguments;

    /**
     * @return a new {@link ReleaseProblemBuilder}
     */
    public static ReleaseProblem.ReleaseProblemBuilder builder() {
        return new ReleaseProblem.ReleaseProblemBuilder();
    }

    /**
     * The builder for {@link ReleaseProblem}.
     *
     * @author christian.heike@icloud.com
     */
    public static class ReleaseProblemBuilder {

        private static final Object[] EMPTY_ARGS = new Object[0];

        private Severity severity;
        private String messageFormat;
        private Object[] formatArgs;

        /**
         * Set the severity for the problem.
         *
         * @param severity the severity
         * @return {@code this}
         */
        @Nonnull
        public ReleaseProblemBuilder severity(@Nonnull final Severity severity) {
            this.severity = severity;
            return this;
        }

        /**
         * Set the message text for formatting.
         *
         * @param message the text
         * @return {@code this}
         */
        @Nonnull
        public ReleaseProblemBuilder messageFormat(@Nonnull final String message) {
            this.messageFormat = message;
            return this;
        }

        /**
         * Set the arguments to format the text with.
         *
         * @param args the arguments
         * @return {@code this}
         */
        @Nonnull
        public ReleaseProblemBuilder formatArgs(@Nullable final Object... args) {
            this.formatArgs = args;
            return this;
        }

        /**
         * Build the {@link ReleaseProblem}.
         *
         * @return the built problem
         */
        @Nonnull
        public ReleaseProblem build() {
            Objects.requireNonNull(severity, "Severity was not set.");
            Objects.requireNonNull(messageFormat, "Message format was not set.");
            return new ReleaseProblem(severity, messageFormat, Optional.ofNullable(formatArgs).orElse(EMPTY_ARGS));
        }

    }
}
