package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import com.google.gson.Gson;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Drop;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemChecker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob.MobChecker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob.MobTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 12/31/2018 at 7:39 PM for the project DungeonRealmsDREnhanced
 */
public class ItemCheckerListener {
    private ItemChecker itemChecker;
    private MobChecker<EntityZombie> zombieMobChecker;
    private MobChecker<EntitySkeleton> skeletonMobChecker;
    private MobChecker<EntityIronGolem> ironGolemMobChecker;
    private MobChecker<EntityWolf> wolfMobChecker;
    private Map<UUID, MobTracker> mobTrackerMap;

    public ItemCheckerListener() {
        this.itemChecker = new ItemChecker(this::onItemFind);
        mobTrackerMap = new ConcurrentHashMap<>();

        this.zombieMobChecker = new MobChecker<>(entityZombie -> {
            MobTracker mobTracker = new MobTracker();
            mobTracker.update(entityZombie);

            mobTrackerMap.put(entityZombie.getUniqueID(), mobTracker);
        }, EntityZombie.class);
        this.ironGolemMobChecker = new MobChecker<>(entityIronGolem -> {
            MobTracker mobTracker = new MobTracker();
            mobTracker.update(entityIronGolem);

            mobTrackerMap.put(entityIronGolem.getUniqueID(), mobTracker);
        }, EntityIronGolem.class);

        this.wolfMobChecker = new MobChecker<>(entityIronGolem -> {
            MobTracker mobTracker = new MobTracker();
            mobTracker.update(entityIronGolem);

            mobTrackerMap.put(entityIronGolem.getUniqueID(), mobTracker);
        }, EntityWolf.class);
        this.skeletonMobChecker = new MobChecker<>(entityZombie -> {
//            ItemRarity best = null;
//
////            message((int) entityZombie.getHealth() + "/" + (int) entityZombie.getMaxHealth());
//            for (ItemStack itemStack : entityZombie.getEquipmentAndArmor()) {
//                ItemRarity rarity = ItemUtils.getRarity(itemStack);
//                if (rarity != null) {
////                    message(entityZombie.getCustomNameTag() + " " + rarity.getTextFormatting() + rarity.getName());
//                    if (rarity.isBetter(best)) {
//                        best = rarity;
//                    }
//                }
//            }
            MobTracker mobTracker = new MobTracker();
            mobTracker.update(entityZombie);

            mobTrackerMap.put(entityZombie.getUniqueID(), mobTracker);
//            if (best != null) {
//                if (best.ordinal() > ItemRarity.COMMON.ordinal()) {
////                    message(entityZombie.getCustomNameTag() + " has " + best.getTextFormatting() + best.getName() + " Item.");
//                    entityZombie.setGlowing(true);
//
//                }
//            }
        }, EntitySkeleton.class);
    }

    @SubscribeEvent
    public void onPlayerDrops(PlayerDropsEvent event) {
        for (EntityItem entityItem : event.getDrops()) {
            entityItem.addTag("M_CHECKED");
        }
    }


    @SubscribeEvent
    public void onRenderGameOverlayText(RenderGameOverlayEvent.Text event) {
        this.zombieMobChecker.update();
        this.skeletonMobChecker.update();
        this.wolfMobChecker.update();
        this.ironGolemMobChecker.update();

    }

    private void onItemFind(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getItem();
//        if (itemStack.getItem() == Items.EMERALD) {
//            entityItem.setCustomNameTag(TextFormatting.GREEN.toString() + itemStack.getCount() + "x Gems");
//            entityItem.setAlwaysRenderNameTag(true);
//
//        }
        if (ItemUtils.isWeapon(itemStack.getItem())) {
            ItemRarity itemRarity = ItemUtils.getRarity(itemStack);
            if (itemRarity == null) {
                return;
            }
            findItem(itemStack);

        } else if (ItemUtils.isArmor(itemStack.getItem())) {
            ItemRarity itemRarity = ItemUtils.getRarity(itemStack);
            if (itemRarity == null) {
                return;
            }
            findItem(itemStack);
//            entityItem.setCustomNameTag(itemRarity.getName());
//            entityItem.setAlwaysRenderNameTag(true);
//            sendMessage(new ScreenMessage("Found " + itemRarity.getName() + " Item", itemRarity.getColor(), 0, 0) {
//                @Override
//                public void render(ScaledResolution scaledResolution) {
//
//                    RenderUtils.drawCenteredStringPlain(this.minecraft.fontRenderer, this.text, (int) scaledResolution.getScaledWidth() / 2, (int) scaledResolution.getScaledHeight() / 2, 2F, itemRarity.getColor());
//                }
//            }, 2000);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (this.mobTrackerMap.containsKey(event.getEntityLiving().getUniqueID())) {
            Entity target = event.getEntity();
            itemChecker.update();
            MobTracker mobTracker = this.mobTrackerMap.get(target.getUniqueID());
            mobTracker.death(target);
            mobTrackerMap.remove(target.getUniqueID());
            mobTrackerMap.put(target.getUniqueID(), mobTracker);

        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {

        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            Entity target = event.getTarget();
            if (target instanceof EntityPlayer) {
                return;
            }
            if (this.mobTrackerMap.containsKey(target.getUniqueID())) {
                MobTracker mobTracker = this.mobTrackerMap.get(target.getUniqueID());
                mobTracker.update(target);
                if (mobTracker.getTier() > -1) {
                    DRPlayer.drPlayer.setLastTier(Tier.getByNumber(mobTracker.getTier()));
                }
                mobTrackerMap.remove(target.getUniqueID());
                mobTrackerMap.put(target.getUniqueID(), mobTracker);
            }
        }
    }

    private void findItem(ItemStack itemStack) {
        Tier tier = ItemUtils.getTier(itemStack);
        if (tier.getNumber() > 0) {

            Drop drop = new Drop(tier, ItemUtils.getRarity(itemStack), (int) DRPlayer.drPlayer.getStatistics().getMobKillCount(tier), DRPlayer.drPlayer.getStatistics().getDryStreak(tier), new Gson().toJson(itemStack.getTagCompound()));
            DRPlayer.drPlayer.addDrop(drop);
            DRPlayer.drPlayer.getStatistics().setDryStreak(tier, -1);
        }

    }
}
