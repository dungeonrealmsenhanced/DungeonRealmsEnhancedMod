package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 3/17/2019 at 7:50 PM for the project drenhancedrestfulapi
 */
public class Changelog {
    private String version;
    private long date;
    private List<Change> changes;

    public Changelog(String version, long date, List<Change> changes) {
        this.version = version;
        this.date = date;
        this.changes = changes;
    }

    private Changelog(Builder builder) {
        version = builder.version;
        date = builder.date;
        changes = builder.changes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Changelog copy) {
        Builder builder = new Builder();
        builder.version = copy.getVersion();
        builder.date = copy.getDate();
        builder.changes = copy.getChanges();
        return builder;
    }

    public String getVersion() {
        return version;
    }

    public long getDate() {
        return date;
    }

    public List<Change> getChanges() {
        return changes;
    }

    /**
     * {@code Changelog} builder static inner class.
     */
    public static final class Builder {
        private String version;
        private long date;
        private List<Change> changes;

        private Builder() {
        }

        /**
         * Sets the {@code version} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param version the {@code version} to set
         * @return a reference to this Builder
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Sets the {@code date} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param date the {@code date} to set
         * @return a reference to this Builder
         */
        public Builder date(long date) {
            this.date = date;
            return this;
        }

        /**
         * Sets the {@code changes} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param changes the {@code changes} to set
         * @return a reference to this Builder
         */
        public Builder changes(Change... changes) {
            if (this.changes == null) {
                this.changes = new ArrayList<>();
            }
            this.changes.addAll(Arrays.asList(changes));
            return this;
        }

        public Builder changes(List<Change> changes) {
            if (this.changes == null) {
                this.changes = new ArrayList<>();
            }
            this.changes.addAll(changes);
            return this;
        }

        /**
         * Returns a {@code Changelog} built from the parameters previously set.
         *
         * @return a {@code Changelog} built with parameters of this {@code Changelog.Builder}
         */
        public Changelog build() {
            return new Changelog(this);
        }


        public static class ChangeListBuilder {
            private List<Change> changes;

            public ChangeListBuilder(List<Change> changes) {
                this.changes = changes;
            }

            public ChangeListBuilder() {
                this.changes = new ArrayList<>();
            }

            public static ChangeListBuilder builder() {
                return new ChangeListBuilder();
            }
            public static ChangeListBuilder builder(List<Change> changes) {
                return new ChangeListBuilder(changes);
            }

            public ChangeListBuilder add(Change change) {
                changes.add(change);
                return this;
            }

            public List<Change> asList() {
                return changes;
            }

        }
    }
}
