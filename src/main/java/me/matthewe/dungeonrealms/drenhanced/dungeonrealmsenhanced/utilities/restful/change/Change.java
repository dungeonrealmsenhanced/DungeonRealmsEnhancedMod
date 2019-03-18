package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change;

/**
 * Created by Matthew E on 3/17/2019 at 7:51 PM for the project drenhancedrestfulapi
 */
public class Change {
    private String title;
    private String[] description;

    public Change(String title, String... description) {
        this.title = title;
        this.description = description;
    }

    private Change(Builder builder) {
        title = builder.title;
        description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Change copy) {
        Builder builder = new Builder();
        builder.title = copy.getTitle();
        builder.description = copy.getDescription();
        return builder;
    }

    public String[] getDescription() {
        return description;
    }


    public String getTitle() {
        return title;
    }

    /**
     * {@code Change} builder static inner class.
     */
    public static final class Builder {
        private String title;
        private String[] description;

        private Builder() {
        }

        /**
         * Sets the {@code title} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param title the {@code title} to set
         * @return a reference to this Builder
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the {@code description} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param description the {@code description} to set
         * @return a reference to this Builder
         */
        public Builder description(String... description) {
            this.description = description;
            return this;
        }

        /**
         * Returns a {@code Change} built from the parameters previously set.
         *
         * @return a {@code Change} built with parameters of this {@code Change.Builder}
         */
        public Change build() {
            return new Change(this);
        }
    }
}
