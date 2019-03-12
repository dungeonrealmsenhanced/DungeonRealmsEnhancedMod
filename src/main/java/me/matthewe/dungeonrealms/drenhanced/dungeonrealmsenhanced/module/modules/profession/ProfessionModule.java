package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced
 */
public class ProfessionModule extends Module {
    public ProfessionModule() {
        super("Profession");
    }

    private int textX = 0;
    private int textY = 0;

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        drawOutline();

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Profession", posX + 2, posY + 2, Tier.T1.getColor());
    }

    private void drawOutline() {
        int[] ints = RenderUtils.drawRectLines(posX, posY, posX + width, posY + height, 1140850688);
        textX = ints[0];
        textY = ints[1];
    }

    public int getTextX() {
        return textX;
    }

    public boolean isMouseWithin(int mouseX, int mouseY) {
        return RenderUtils.isMouseWithin(mouseX, mouseY, posX, posY, width, height);
    }

    public int getTextY() {
        return textY;
    }

    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        this.width = 135;
        this.height = 85;
    }

    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        drawOutline();
    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {

        DRPlayer drPlayer = DRPlayer.get();

        EntityPlayerSP player = mc.player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
            return;
        }
        if ((drPlayer != null) && (player != null) && ItemType.isProfessionItem(itemStack)) {
            ProfessionItem professionItem = ProfessionItem.of(itemStack);
            if (professionItem != null) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(itemStack.getDisplayName(), posX + 2, posY + 2, Tier.T5.getColor());
                int y = posY + 5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;

                for (String lore  :ItemUtils.getLore(itemStack)){
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(lore , posX + 2, y, Tier.T5.getColor());
                    y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
                }
            }
        }
    }

    @Override
    public void onLoad() {
        this.setEnabled(true);
        this.posX = 5;
        this.posY = 190;

    }

    @Override
    public void onUnload() {

    }
}


