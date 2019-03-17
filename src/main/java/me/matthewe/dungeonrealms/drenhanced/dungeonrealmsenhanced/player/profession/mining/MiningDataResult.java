package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

/**
 * Created by Matthew E on 3/13/2019 at 10:26 AM for the project DungeonRealmsDREnhanced
 */
public class MiningDataResult {
    private int currentTierOreRemaining;
    private int currentTierFailCount;
    private int currentTierSuccessCount;

    private int lowerTierOreRemaining;
    private int lowerTierFailCount;
    private int lowerTierSuccessCount;

    private int averageCurrentExperience;
    private int averageLowerExperience;

    public MiningDataResult(int currentTierOreRemaining, int currentTierFailCount, int currentTierSuccessCount, int lowerTierOreRemaining, int lowerTierFailCount, int lowerTierSuccessCount, int averageCurrentExperience, int averageLowerExperience) {
        this.currentTierOreRemaining = currentTierOreRemaining;
        this.currentTierFailCount = currentTierFailCount;
        this.currentTierSuccessCount = currentTierSuccessCount;
        this.lowerTierOreRemaining = lowerTierOreRemaining;
        this.lowerTierFailCount = lowerTierFailCount;
        this.lowerTierSuccessCount = lowerTierSuccessCount;
        this.averageCurrentExperience = averageCurrentExperience;
        this.averageLowerExperience = averageLowerExperience;
    }

    private MiningDataResult(Builder builder) {
        currentTierOreRemaining = builder.currentTierOreRemaining;
        currentTierFailCount = builder.currentTierFailCount;
        currentTierSuccessCount = builder.currentTierSuccessCount;
        lowerTierOreRemaining = builder.lowerTierOreRemaining;
        lowerTierFailCount = builder.lowerTierFailCount;
        lowerTierSuccessCount = builder.lowerTierSuccessCount;
        this.averageCurrentExperience = builder.averageCurrentExperience;
        this.averageLowerExperience = builder.averageLowerExperience;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getAverageCurrentExperience() {
        return averageCurrentExperience;
    }

    public int getAverageLowerExperience() {
        return averageLowerExperience;
    }

    public static Builder builder(MiningDataResult copy) {
        Builder builder = new Builder();
        builder.currentTierOreRemaining = copy.getCurrentTierOreRemaining();
        builder.currentTierFailCount = copy.getCurrentTierFailCount();
        builder.currentTierSuccessCount = copy.getCurrentTierSuccessCount();
        builder.lowerTierOreRemaining = copy.getLowerTierOreRemaining();
        builder.lowerTierFailCount = copy.getLowerTierFailCount();
        builder.lowerTierSuccessCount = copy.getLowerTierSuccessCount();
        builder.averageLowerExperience = copy.getAverageLowerExperience();
        builder.averageCurrentExperience = copy.getAverageCurrentExperience();
        return builder;
    }


    public int getCurrentTierOreRemaining() {
        return currentTierOreRemaining;
    }

    public int getCurrentTierFailCount() {
        return currentTierFailCount;
    }

    public int getCurrentTierSuccessCount() {
        return currentTierSuccessCount;
    }

    public int getLowerTierOreRemaining() {
        return lowerTierOreRemaining;
    }

    public int getLowerTierFailCount() {
        return lowerTierFailCount;
    }

    public int getLowerTierSuccessCount() {
        return lowerTierSuccessCount;
    }

    /**
     * {@code MiningDataResult} builder static inner class.
     */
    public static final class Builder {
        private int currentTierOreRemaining;
        private int currentTierFailCount;
        private int currentTierSuccessCount;
        private int lowerTierOreRemaining;
        private int lowerTierFailCount;
        private int lowerTierSuccessCount;
        private int averageCurrentExperience;
        private int averageLowerExperience;

        private Builder() {
        }

        /**
         * Sets the {@code currentTierOreRemaining} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param currentTierOreRemaining the {@code currentTierOreRemaining} to set
         * @return a reference to this Builder
         */
        public Builder currentTierOreRemaining(int currentTierOreRemaining) {
            this.currentTierOreRemaining = currentTierOreRemaining;
            return this;
        }

        public Builder averageCurrentExperience(int averageCurrentExperience) {
            this.averageCurrentExperience = averageCurrentExperience;
            return this;
        }

        public Builder averageLowerExperience(int averageLowerExperience) {
            this.averageLowerExperience = averageLowerExperience;
            return this;
        }

        /**
         * Sets the {@code currentTierFailCount} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param currentTierFailCount the {@code currentTierFailCount} to set
         * @return a reference to this Builder
         */
        public Builder currentTierFailCount(int currentTierFailCount) {
            this.currentTierFailCount = currentTierFailCount;
            return this;
        }

        /**
         * Sets the {@code currentTierSuccessCount} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param currentTierSuccessCount the {@code currentTierSuccessCount} to set
         * @return a reference to this Builder
         */
        public Builder currentTierSuccessCount(int currentTierSuccessCount) {
            this.currentTierSuccessCount = currentTierSuccessCount;
            return this;
        }

        /**
         * Sets the {@code lowerTierOreRemaining} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param lowerTierOreRemaining the {@code lowerTierOreRemaining} to set
         * @return a reference to this Builder
         */
        public Builder lowerTierOreRemaining(int lowerTierOreRemaining) {
            this.lowerTierOreRemaining = lowerTierOreRemaining;
            return this;
        }

        /**
         * Sets the {@code lowerTierFailCount} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param lowerTierFailCount the {@code lowerTierFailCount} to set
         * @return a reference to this Builder
         */
        public Builder lowerTierFailCount(int lowerTierFailCount) {
            this.lowerTierFailCount = lowerTierFailCount;
            return this;
        }

        /**
         * Sets the {@code lowerTierSuccessCount} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param lowerTierSuccessCount the {@code lowerTierSuccessCount} to set
         * @return a reference to this Builder
         */
        public Builder lowerTierSuccessCount(int lowerTierSuccessCount) {
            this.lowerTierSuccessCount = lowerTierSuccessCount;
            return this;
        }

        /**
         * Returns a {@code MiningDataResult} built from the parameters previously set.
         *
         * @return a {@code MiningDataResult} built with parameters of this {@code MiningDataResult.Builder}
         */
        public MiningDataResult build() {
            return new MiningDataResult(this);
        }
    }
}
