package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.Collection;

/**
 * Created by Matthew E on 3/1/2025 at 12:47 AM EST for the project DungeonRealmsDREnhanced
 */
public class ShardCountModule extends Module {
    public ShardCountModule() {
        super("ShardCount");
    }

    public static void setCount(int count) {
        ShardCountModule.count = count;
    }

    private static int count;
    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getTextToShow(), this.posX, this.posY, 0xFFFF55);
    }

    private final DecimalFormat format = new DecimalFormat("#,###");

    private String getTextToShow(){



        return TextFormatting.YELLOW +"Player Count: " + TextFormatting.WHITE+format.format(count);

    }
    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        int[] ints = RenderUtils.getTextBoxDimentions(posX-2, posY-2, getTextToShow());
        this.width = ints[0];
        this.height = ints[1];
    }

    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        RenderUtils.drawRectLines(posX-2, posY-2, this.width, this.height,1140850688);

    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(getTextToShow(), this.posX, this.posY, Tier.T2.getColor());
    }

    @Override
    public void onLoad() {
        this.posX = 150;
        this.posY = 250;
    }

    @Override
    public void onUnload() {

    }
}
