package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.GuiOverlapEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenRenderer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.DRTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by Matthew E on 3/11/2019 at 8:59 AM for the project DungeonRealmsDREnhanced
 */
public class RarityOverlayListener {

    @SubscribeEvent
    public void onPlayerInventory(GuiOverlapEvent.InventoryOverlap.DrawGuiContainerForegroundLayer event) {
        for (Slot slot : event.getGuiInventory().inventorySlots.inventorySlots) {
            ItemStack itemStack = slot.getStack();
            float r;
            float g;
            float b;

            if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
                continue;
            }
            ItemRarity itemRarity = ItemRarity.getByItemStack(itemStack);
            if (itemRarity != null) {
                r = itemRarity.getRgbaColor().r;
                g = itemRarity.getRgbaColor().g;
                b = itemRarity.getRgbaColor().b;
                renderItemOverlay(r, g, b, slot.xPos, slot.yPos);
            }
        }

    }


    private void renderItemOverlay( float r, float g, float b, int xPos, int yPos) {
        ScreenRenderer.beginGL(0, 0);
        ScreenRenderer renderer = new ScreenRenderer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(r, g, b, 1.0f);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
        renderer.drawRect(DRTextures.RARITY_OVERLAY.get(), xPos - 1, yPos - 1, 0, 0, 18, 18);
        GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        ScreenRenderer.endGL();
    }
}
