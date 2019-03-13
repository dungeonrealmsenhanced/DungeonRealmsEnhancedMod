package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import java.util.UUID;

/**
 * Created by Matthew E on 3/13/2019 at 9:28 AM for the project DungeonRealmsDREnhanced
 */
public class MiningRequest {
    private UUID uuid;
    private MiningRequestType miningRequestType;
    private MiningRequestResult result;

    public MiningRequest(MiningRequestType miningRequestType, MiningRequestResult result) {
        this.uuid = UUID.randomUUID();
        this.miningRequestType = miningRequestType;
        this.result = result;
    }

    public UUID getUuid() {
        return uuid;
    }

    public MiningRequestType getMiningRequestType() {
        return miningRequestType;
    }

    public MiningRequestResult getResult() {
        return result;
    }
}
