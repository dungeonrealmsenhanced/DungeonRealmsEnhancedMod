package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful;

/**
 * Created by Matthew Eisenberg on 3/16/2019 at 2:37 PM for the project DungeonRealmsDREnhanced
 */
public class DREnhancedInformation {
    private String version;
    private Developer[] developers;
    private String source;

    public DREnhancedInformation(String version, Developer[] developers, String source) {
        this.version = version;
        this.developers = developers;
        this.source = source;
    }

    public String getVersion() {
        return version;
    }

    public Developer[] getDevelopers() {
        return developers;
    }

    public String getSource() {
        return source;
    }

    public static class Developer {
        private String uuid;
        private String username;
        private boolean mainDeveloper;

        public Developer(String uuid, String username, boolean mainDeveloper) {
            this.uuid = uuid;
            this.username = username;
            this.mainDeveloper = mainDeveloper;
        }

        public String getUuid() {
            return uuid;
        }

        public String getUsername() {
            return username;
        }

        public boolean isMainDeveloper() {
            return mainDeveloper;
        }
    }
}
