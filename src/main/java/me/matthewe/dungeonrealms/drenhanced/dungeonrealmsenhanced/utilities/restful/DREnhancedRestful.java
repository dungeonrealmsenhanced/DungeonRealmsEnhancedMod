package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful;

import java.util.function.Consumer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change.Change;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change.Changelog;

public class DREnhancedRestful {
  private DREnhancedInformation information;

  public DREnhancedRestful() {
    information =
        new DREnhancedInformation(
            "2.0.0+mc26.3",
            new DREnhancedInformation.Developer[] {
              new DREnhancedInformation.Developer(
                  "1d48bd80-4cd0-4874-ba65-94284bc24ecc", "MatthewEDev", true)
            },
            "N/A",
            Changelog.builder()
                .date(1740704964255L)
                .changes(
                    Change.builder()
                        .title("Additions")
                        .description(
                            "Orb & stat % added"
                                + "DMG %"
                                + "Energy %"
                                + "Shard player count display"
                                + "Weapon DPS"
                                + "Restful API so values can be updated dynamically. ")
                        .build(),
                    Change.builder().title("Fixes").description("Debug spam issue.").build())
                .version("2.0.0+mc26.3")
                .build()); // Hardcoded changelogs for now
  }

  public DREnhancedInformation getInformation() {
    return information;
  }

  @Deprecated
  public void update(Consumer<DREnhancedInformation> consumer) {
    consumer.accept(information);
  }
}
