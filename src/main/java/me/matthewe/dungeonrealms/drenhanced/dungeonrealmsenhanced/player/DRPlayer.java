package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffRequest;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffRequestReturn;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.Mining;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.SettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.Settings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Drop;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 12/31/2018 at 2:32 PM for the project DungeonRealmsDREnhanced
 */
public class DRPlayer {
    private UUID uuid;
    private Statistics statistics;
    private Tier lastTier;
    private Location location;
    private Settings settings = new Settings();
    private DRPlayerType drPlayerType;
    private Map<Long, Integer> damages = new ConcurrentHashMap<>();
    private String version;
    private Mining mining = new Mining();
    private boolean loaded;
    public List<Drop> drops = new ArrayList<>();
    private List<String> friends = new ArrayList<>();
    private List<Long> clicks = new ArrayList<>();

    public static DRPlayer drPlayer = new DRPlayer(UUID.randomUUID(), Statistics.getDefaultStatistics(), new ArrayList<>(), new Settings(), DRPlayerType.REAL, new Mining(), DREnhanced.VERSION);


    public static DRPlayer get() {
        if (drPlayer == null) {
            drPlayer = new DRPlayer(UUID.randomUUID(), Statistics.getDefaultStatistics(), new ArrayList<>(), new Settings(), DRPlayerType.REAL, new Mining(), DREnhanced.VERSION);
        }
        return drPlayer;
    }

    public Location getLocation() {
        if (location == null) {
            location = new Location(0, 0, 0, 0, 0);
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addDrop(Drop drop) {
        if (this.drops == null) {
            this.drops = new ArrayList<>();
        }
        this.drops.add(drop);
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public void addDamage(int damage) {
        damages.put(Long.valueOf(System.currentTimeMillis()), damage);
    }

    public void addClick() {
        clicks.add(Long.valueOf(System.currentTimeMillis()));
    }

    public int getCPS() {
        Iterator<Long> iterator = clicks.iterator();
        while (iterator.hasNext()) {
            long longValue = ((Long) iterator.next()).longValue();
            if (longValue < System.currentTimeMillis() - 1000L) {
                iterator.remove();
            }
        }
        return clicks.size();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getDPS() {
        Iterator<Long> iterator = damages.keySet().iterator();
        int totalDamage = 0;
        List<Long> toRemoveList = new ArrayList<>();
        while (iterator.hasNext()) {
            long longValue = ((Long) iterator.next()).longValue();
            if (longValue < System.currentTimeMillis() - 1000L) {
                iterator.remove();
                toRemoveList.add(longValue);
            }
        }
        for (Long aLong : toRemoveList) {
            if (damages.containsKey(aLong)) {
                damages.remove(aLong);
            }
        }
        for (Integer value : damages.values()) {
            totalDamage += value;
        }

        return totalDamage;
    }

    public Settings getSettings() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setDrPlayerType(DRPlayerType drPlayerType) {
        this.drPlayerType = drPlayerType;
    }

    public DRPlayerType getDrPlayerType() {
        return drPlayerType;
    }

    public DRPlayer(UUID uuid, Statistics statistics, List<Drop> drops, Settings settings, DRPlayerType drPlayerType, Mining mining, String version) {
        this.uuid = uuid;
        this.statistics = statistics;
        this.drops = drops;
        this.settings = settings;
        this.drPlayerType = drPlayerType;
        this.mining = mining;
        this.version = version;

    }

    public UUID getUuid() {
        return uuid;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Tier getLastTier() {
        if (lastTier == null) {
            lastTier = Tier.T1;
        }
        return lastTier;
    }

    public void setMining(Mining mining) {
        this.mining = mining;
    }

    public Mining getMining() {
        return mining;
    }

    public void setLastTier(Tier lastTier) {
        this.lastTier = lastTier;
    }

    public int getLastDryCount() {
        if (lastTier == null) {
            return 0;
        }
        return statistics.getDryStreak(lastTier);
    }

    public void setSettingValue(DRSettings key, Object o) {
        for (SettingCategory value : settings.getCategoryMap().values()) {
            if (value.getValueMap().containsKey(key)) {
                value.setSettingValue(key, o);
                break;
            }
        }
    }

    private String percentFormat = "###.##";

    public void update() {
        this.updateGamma();
        this.updatePercentFormat();


    }

    public void setPercentFormat(String percentFormat) {
        this.percentFormat = percentFormat;
    }

    public String getPercentFormat() {
        return percentFormat;
    }

    private void updatePercentFormat() {
        boolean error = false;
        try {

            new DecimalFormat(DRSettings.MISC_PERCENT_FORMAT.get(String.class)).format(344.3223);
            error = false;
        } catch (Exception e) {
            this.percentFormat = "###.##";
            DRPlayer.get().setSettingValue(DRSettings.MISC_PERCENT_FORMAT, this.percentFormat);
            error = true;

        }
        if (!error) {
            this.percentFormat = DRSettings.MISC_PERCENT_FORMAT.get(String.class);
        }

    }

    private void updateGamma() {
        if (DRSettings.GAMMA.get(double.class) != Minecraft.getMinecraft().gameSettings.gammaSetting) {
            if (DRSettings.GAMMA.get(double.class) <= 1) {
//                Minecraft.getMinecraft().gameSettings.gammaSetting = 1;
            } else {
                Minecraft.getMinecraft().gameSettings.gammaSetting = DRSettings.GAMMA.get(double.class).floatValue();
            }
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void fixDryStreak() {
        List<Tier> resetDryStreakList = new ArrayList<>();
        statistics.getDryStreaks().entrySet().stream().filter(entry -> entry.getValue() < 0).forEach(entry -> resetDryStreakList.add(entry.getKey()));
        for (Tier tier : resetDryStreakList) {
            statistics.setDryStreak(tier, 0);
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void clearToSave() {
        this.clicks.clear();
        this.damages.clear();
    }

    public void resetMining() {
        mining =new Mining();

    }



    public static class Statistics {
        private int playerKills = 0;
        private int deaths = 0;
        private long playTime = 0;
        private int level = 1;
        private long bankGems = 0;
        private boolean inGuild = false;
        private int fishCaught = 0;
        private int oreMined = 0;
        private int lootOpened = 0;
        private Map<Tier, Long> mobKills = new ConcurrentHashMap<>();
        private Map<Tier, Integer> dryStreaks = new ConcurrentHashMap<>();

        public Statistics(int playerKills, int deaths, long playTime, int level, long bankGems, boolean inGuild, int fishCaught, int oreMined, int lootOpened, Map<Tier, Long> mobKills, Map<Tier, Integer> dryStreaks) {
            this.playerKills = playerKills;
            this.deaths = deaths;
            this.playTime = playTime;
            this.level = level;
            this.bankGems = bankGems;
            this.inGuild = inGuild;
            this.fishCaught = fishCaught;
            this.oreMined = oreMined;
            this.lootOpened = lootOpened;
            this.mobKills = mobKills;
            this.dryStreaks = dryStreaks;
        }

        private Statistics(Builder builder) {
            setPlayerKills(builder.playerKills);
            setDeaths(builder.deaths);
            setPlayTime(builder.playTime);
            setLevel(builder.level);
            setBankGems(builder.bankGems);
            setInGuild(builder.inGuild);
            setFishCaught(builder.fishCaught);
            setOreMined(builder.oreMined);
            setLootOpened(builder.lootOpened);
            setMobKills(builder.mobKills);
            setDryStreaks(builder.dryStreaks);
        }

        public Map<Tier, Integer> getDryStreaks() {
            return dryStreaks;
        }

        public void setDryStreaks(Map<Tier, Integer> dryStreaks) {
            this.dryStreaks = dryStreaks;
        }

        public static Statistics getDefaultStatistics() {
            Map<Tier, Long> mobKills = new ConcurrentHashMap<>();
            for (Tier value : Tier.values()) {
                mobKills.put(value, 0L);
            }
            Map<Tier, Integer> dryStreaks = new ConcurrentHashMap<>();
            for (Tier value : Tier.values()) {
                dryStreaks.put(value, 0);
            }
            return Statistics.builder()
                    .mobKills(mobKills)
                    .dryStreaks(dryStreaks)
                    .build();
        }

        public int getDryStreak(Tier tier) {
            if ((dryStreaks == null) || dryStreaks.isEmpty()) {
                this.dryStreaks = new ConcurrentHashMap<>();
                for (Tier value : Tier.values()) {
                    dryStreaks.put(value, 0);
                }
            }
            return dryStreaks.getOrDefault(tier, 0);
        }

        public void increaseDryStreak(Tier tier, int amount) {
            setDryStreak(tier, getDryStreak(tier) + amount);
        }

        public void setDryStreak(Tier tier, int amount) {
            getDryStreak(tier);
            if (this.dryStreaks.containsKey(tier)) {
                this.dryStreaks.remove(tier);
            }
            this.dryStreaks.put(tier, amount);
        }

        public void increaseKillCount(Tier tier) {
            setMobKillCount(tier, getMobKillCount(tier) + 1);
        }

        public void setMobKillCount(Tier tier, long count) {
            long mobKillCount = getMobKillCount(tier);
            if (this.mobKills.containsKey(tier)) {
                this.mobKills.remove(tier);
            }
            this.mobKills.put(tier, count);

        }

        public long getMobKillCount(Tier tier) {
            if (this.mobKills == null) {
                this.mobKills = new ConcurrentHashMap<>();
                for (Tier value : Tier.values()) {
                    this.mobKills.put(value, 0L);
                }
            }
            return mobKills.get(tier);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder builder(Statistics copy) {
            Builder builder = new Builder();
            builder.playerKills = copy.getPlayerKills();
            builder.deaths = copy.getDeaths();
            builder.playTime = copy.getPlayTime();
            builder.level = copy.getLevel();
            builder.bankGems = copy.getBankGems();
            builder.inGuild = copy.isInGuild();
            builder.fishCaught = copy.getFishCaught();
            builder.oreMined = copy.getOreMined();
            builder.lootOpened = copy.getLootOpened();
            builder.mobKills = copy.getMobKills();
            builder.dryStreaks = copy.getDryStreaks();
            return builder;
        }

        public int getPlayerKills() {
            return playerKills;
        }

        public void setPlayerKills(int playerKills) {
            this.playerKills = playerKills;
        }

        public int getDeaths() {
            return deaths;
        }

        public void setDeaths(int deaths) {
            this.deaths = deaths;
        }

        public long getPlayTime() {
            return playTime;
        }

        public void setPlayTime(long playTime) {
            this.playTime = playTime;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public long getBankGems() {
            return bankGems;
        }

        public void setBankGems(long bankGems) {
            this.bankGems = bankGems;
        }

        public boolean isInGuild() {
            return inGuild;
        }

        public void setInGuild(boolean inGuild) {
            this.inGuild = inGuild;
        }

        public int getFishCaught() {
            return fishCaught;
        }

        public void setFishCaught(int fishCaught) {
            this.fishCaught = fishCaught;
        }

        public int getOreMined() {
            return oreMined;
        }

        public void setOreMined(int oreMined) {
            this.oreMined = oreMined;
        }

        public int getLootOpened() {
            return lootOpened;
        }

        public void setLootOpened(int lootOpened) {
            this.lootOpened = lootOpened;
        }

        public Map<Tier, Long> getMobKills() {
            return mobKills;
        }

        public void setMobKills(Map<Tier, Long> mobKills) {
            this.mobKills = mobKills;
        }

        /**
         * {@code Statistics} builder static inner class.
         */
        public static final class Builder {
            private int playerKills = 0;
            private int deaths = 0;
            private long playTime = 0;
            private int level = 0;
            private long bankGems = 0;
            private boolean inGuild = false;
            private int fishCaught = 0;
            private int oreMined = 0;
            private int lootOpened = 0;
            private Map<Tier, Long> mobKills = new ConcurrentHashMap<>();
            private Map<Tier, Integer> dryStreaks = new ConcurrentHashMap<>();

            private Builder() {
            }

            /**
             * Sets the {@code playerKills} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param playerKills the {@code playerKills} to set
             * @return a reference to this Builder
             */
            public Builder playerKills(int playerKills) {
                this.playerKills = playerKills;
                return this;
            }

            /**
             * Sets the {@code deaths} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param deaths the {@code deaths} to set
             * @return a reference to this Builder
             */
            public Builder deaths(int deaths) {
                this.deaths = deaths;
                return this;
            }

            public Builder dryStreaks(Map<Tier, Integer> dryStreaks) {
                this.dryStreaks = dryStreaks;
                return this;
            }

            /**
             * Sets the {@code playTime} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param playTime the {@code playTime} to set
             * @return a reference to this Builder
             */
            public Builder playTime(long playTime) {
                this.playTime = playTime;
                return this;
            }

            /**
             * Sets the {@code level} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param level the {@code level} to set
             * @return a reference to this Builder
             */
            public Builder level(int level) {
                this.level = level;
                return this;
            }

            /**
             * Sets the {@code bankGems} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param bankGems the {@code bankGems} to set
             * @return a reference to this Builder
             */
            public Builder bankGems(long bankGems) {
                this.bankGems = bankGems;
                return this;
            }

            /**
             * Sets the {@code inGuild} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param inGuild the {@code inGuild} to set
             * @return a reference to this Builder
             */
            public Builder inGuild(boolean inGuild) {
                this.inGuild = inGuild;
                return this;
            }

            /**
             * Sets the {@code fishCaught} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param fishCaught the {@code fishCaught} to set
             * @return a reference to this Builder
             */
            public Builder fishCaught(int fishCaught) {
                this.fishCaught = fishCaught;
                return this;
            }

            /**
             * Sets the {@code oreMined} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param oreMined the {@code oreMined} to set
             * @return a reference to this Builder
             */
            public Builder oreMined(int oreMined) {
                this.oreMined = oreMined;
                return this;
            }

            /**
             * Sets the {@code lootOpened} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param lootOpened the {@code lootOpened} to set
             * @return a reference to this Builder
             */
            public Builder lootOpened(int lootOpened) {
                this.lootOpened = lootOpened;
                return this;
            }

            /**
             * Sets the {@code mobKills} and returns a reference to this Builder so that the methods can be chained together.
             *
             * @param mobKills the {@code mobKills} to set
             * @return a reference to this Builder
             */
            public Builder mobKills(Map<Tier, Long> mobKills) {
                this.mobKills = mobKills;
                return this;
            }

            /**
             * Returns a {@code Statistics} built from the parameters previously set.
             *
             * @return a {@code Statistics} built with parameters of this {@code Statistics.Builder}
             */
            public Statistics build() {
                return new Statistics(this);
            }
        }
    }
}
