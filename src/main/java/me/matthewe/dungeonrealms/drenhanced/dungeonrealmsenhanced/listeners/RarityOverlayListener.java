package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.GuiOverlapEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.color.RGBAColor;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenRenderer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.DRTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Matthew E on 3/11/2019 at 8:59 AM for the project DungeonRealmsDREnhanced
 */
public class RarityOverlayListener {

    public static int inventoryGems = 0;

    @SubscribeEvent
    public void onPlayerInventory(GuiOverlapEvent.InventoryOverlap.DrawGuiContainerForegroundLayer event) {
        drawOverlay(event.getGuiInventory().inventorySlots.inventorySlots);
        drawGems();
    }

    @SubscribeEvent
    public void onChestInventory(GuiOverlapEvent.ChestOverlap.DrawGuiContainerForegroundLayer event) {
        drawOverlay(event.getGuiInventory().inventorySlots.inventorySlots);
    }

    private int getGems() {
        int gems = 0;
        for (ItemStack itemStack : Minecraft.getMinecraft().player.inventory.mainInventory) {
            if (itemStack == null || itemStack.getCount() == 0) {
                continue;
            }
            if (itemStack.getItem() == Items.EMERALD) {
                gems += itemStack.getCount();
            }
            if (itemStack.hasTagCompound() && (itemStack.getTagCompound() != null) && itemStack.getTagCompound().hasKey("gemValue")) {
                gems += itemStack.getTagCompound().getInteger("gemValue");
            }
        }
        inventoryGems= gems;
        return gems;
    }

    private void drawGems() {
        if (!DRSettings.GEM_COUNT_INVENTORY_OVERLAY.get(boolean.class)) {
            return;
        }
        GlStateManager.disableLighting();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
        int x = 145;
        int gems = getGems();
        int y = (int) ((Minecraft.getMinecraft().player.inventory.mainInventory.size() / 9) * 19.7) - 3;
        ScreenRenderer.beginGL(0, 0);
        {
            ScreenRenderer.scale(0.9f);
            Minecraft.getMinecraft().fontRenderer.drawString(decimalFormat.format(gems) + TextFormatting.BOLD + "g", x, y, 4210752, false);
        }
        ScreenRenderer.endGL();
        GlStateManager.enableLighting();
    }


    private void drawOverlay(List<Slot> slots) {
        for (Slot slot : slots) {
            ItemStack itemStack = slot.getStack();
            float r;
            float g;
            float b;
            if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
                continue;
            }
            if (ItemType.isClueScroll(itemStack) && DRSettings.GLOWING_CLUE_SCROLL.get(boolean.class)) {
                RGBAColor rgbaColor = new RGBAColor(1.0f, 0.0f, 0.0f);
                r = rgbaColor.r;
                g = rgbaColor.g;
                b = rgbaColor.b;
                renderItemOverlay(r, g, b, slot.xPos, slot.yPos);
                continue;
            }
            ItemRarity itemRarity = ItemRarity.getByItemStack(itemStack);
            boolean mythic = ItemUtils.isMythic(itemStack);

            if (itemRarity != null || mythic) {
                if (itemRarity != null) {
                    switch (itemRarity) {
                        case COMMON:
                            if (!DRSettings.GLOWING_RARITIES_COMMON.get(boolean.class)) {
                                continue;
                            }
                            break;
                        case UNCOMMON:
                            if (!DRSettings.GLOWING_RARITIES_UNCOMMON.get(boolean.class)) {
                                continue;
                            }
                            break;
                        case RARE:
                            if (!DRSettings.GLOWING_RARITIES_RARE.get(boolean.class)) {
                                continue;
                            }
                            break;
                        case EPIC:
                            if (!DRSettings.GLOWING_RARITIES_EPIC.get(boolean.class)) {
                                continue;
                            }
                            break;
                        case LEGENDARY:
                            if (!DRSettings.GLOWING_RARITIES_LEGENDARY.get(boolean.class)) {
                                continue;
                            }
                            break;
                    }
                }

                if (mythic) {
                    if (!DRSettings.GLOWING_RARITIES_MYTHIC.get(boolean.class)) {
                        itemRarity = null;
                    } else {
                        itemRarity = ItemRarity.MYTHIC;
                    }
                }
                if (itemRarity != null) {
                    r = itemRarity.getRgbaColor().r;
                    g = itemRarity.getRgbaColor().g;
                    b = itemRarity.getRgbaColor().b;
                    renderItemOverlay(r, g, b, slot.xPos, slot.yPos);
                }
            }
        }
    }

    private void renderItemOverlay(float r, float g, float b, int xPos, int yPos) {
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
