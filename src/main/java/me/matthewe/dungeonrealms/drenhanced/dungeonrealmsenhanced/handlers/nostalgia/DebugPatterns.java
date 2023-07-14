package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matthew E on 3/17/2019 at 4:21 PM for the project DungeonRealmsDREnhanced
 */
public enum DebugPatterns {
    DEBUG_PATTERN(Pattern.compile("(\\d+.) DMG ->+(.*)")),
//    DEBUG_DAMAGE_TAKEN_PATTERN(Pattern.compile("-(\\d+.)\\sHP(.*)"));
    DEBUG_DAMAGE_TAKEN_PATTERN(Pattern.compile("-([0-9]*)\\sHP(.*)"));
//    private Pattern pattern = Pattern.compile(".*\\+([0-9]*)\\sEXP\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
    private Pattern pattern;

    DebugPatterns(Pattern pattern) {
        this.pattern = pattern;
    }

    public Matcher matcher(String input) {
        return pattern.matcher(input);
    }
}
